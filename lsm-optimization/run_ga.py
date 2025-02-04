#!/usr/bin/env python3
"""
Hybrid LSM optimization using a binary GA with hyperparameter tuning via Optuna
and local refinement to select a feasible subset of settlements.
"""

import json
import time
import numpy as np
import random
from deap import base, creator, tools
import multiprocessing
import optuna
from functools import partial


# ----------------------------
# Load JSON data and preprocess
# ----------------------------
def load_data(filename):
    with open(filename, "r") as f:
        data = json.load(f)
    balances_raw = data["balances"]
    if isinstance(balances_raw, list):
        balances = {b["client"]: {"cashAmount": b["cashAmount"], "tokenAmount": b["tokenAmount"]}
                    for b in balances_raw}
    else:
        balances = balances_raw
    settlements = data["settlements"]
    for s in settlements:
        if "cashAmount" not in s and "cash" in s:
            s["cashAmount"] = s["cash"]
        if "tokenAmount" not in s and "token" in s:
            s["tokenAmount"] = s["token"]
    return balances, settlements


# ----------------------------
# Preprocess data: convert to NumPy arrays
# ----------------------------
def preprocess_data(balances, settlements):
    clients = list(balances.keys())
    client_to_idx = {client: i for i, client in enumerate(clients)}
    n_clients = len(clients)
    client_cash = np.zeros(n_clients)
    client_token = np.zeros(n_clients)
    for client, data in balances.items():
        i = client_to_idx[client]
        client_cash[i] = data["cashAmount"]
        client_token[i] = data["tokenAmount"]
    settlement_cash = np.array([s["cashAmount"] for s in settlements])
    settlement_token = np.array([s["tokenAmount"] for s in settlements])
    settlement_buyer = np.array([client_to_idx[s["buyer"]] for s in settlements])
    settlement_seller = np.array([client_to_idx[s["seller"]] for s in settlements])
    return client_cash, client_token, settlement_cash, settlement_token, settlement_buyer, settlement_seller, clients


# ----------------------------
# Helper: Check feasibility of an individual
# ----------------------------
def is_feasible(individual, client_cash, client_token, settlement_cash, settlement_token,
                settlement_buyer, settlement_seller):
    selected = np.array(individual, dtype=bool)
    net_cash = np.zeros_like(client_cash)
    net_token = np.zeros_like(client_token)
    np.add.at(net_cash, settlement_buyer[selected], -settlement_cash[selected])
    np.add.at(net_cash, settlement_seller[selected], settlement_cash[selected])
    np.add.at(net_token, settlement_buyer[selected], settlement_token[selected])
    np.add.at(net_token, settlement_seller[selected], -settlement_token[selected])
    final_cash = client_cash + net_cash
    final_token = client_token + net_token
    return (final_cash >= 0).all() and (final_token >= 0).all()


# ----------------------------
# Repair operator: iteratively remove settlements causing negative balances.
# ----------------------------
def repair_individual(individual, client_cash, client_token, settlement_cash, settlement_token,
                      settlement_buyer, settlement_seller):
    candidate = individual[:]  # Make a copy
    # Loop until the candidate is feasible or no further repair is possible.
    while True:
        selected = np.array(candidate, dtype=bool)
        net_cash = np.zeros_like(client_cash)
        net_token = np.zeros_like(client_token)
        np.add.at(net_cash, settlement_buyer[selected], -settlement_cash[selected])
        np.add.at(net_cash, settlement_seller[selected], settlement_cash[selected])
        np.add.at(net_token, settlement_buyer[selected], settlement_token[selected])
        np.add.at(net_token, settlement_seller[selected], -settlement_token[selected])
        final_cash = client_cash + net_cash
        final_token = client_token + net_token

        # Check feasibility: if all clients have non-negative balances, we're done.
        if (final_cash >= 0).all() and (final_token >= 0).all():
            break

        # Identify the client with the greatest violation.
        violation_cash = np.maximum(0, -final_cash)
        violation_token = np.maximum(0, -final_token)
        total_violation = violation_cash + violation_token
        client_idx = np.argmax(total_violation)

        # Find candidate settlements involving this client.
        candidate_indices = []
        if final_cash[client_idx] < 0:
            candidate_indices.extend(np.where(settlement_buyer == client_idx)[0])
        if final_token[client_idx] < 0:
            candidate_indices.extend(np.where(settlement_seller == client_idx)[0])
        candidate_indices = list(set(candidate_indices))
        # Choose the settlement that contributes most to the violation.
        worst = None
        worst_effect = 0
        for i in candidate_indices:
            if candidate[i] == 1:
                effect = 0
                if settlement_buyer[i] == client_idx:
                    effect += settlement_cash[i]
                if settlement_seller[i] == client_idx:
                    effect += settlement_token[i]
                if effect > worst_effect:
                    worst_effect = effect
                    worst = i
        if worst is None:
            break
        candidate[worst] = 0  # Remove the worst offending settlement.
    return candidate


# ----------------------------
# Global evaluation function (vectorized) with continuous penalty
# ----------------------------
def evaluate_individual_vectorized(individual, client_cash, client_token, settlement_cash, settlement_token,
                                   settlement_buyer, settlement_seller, W_count=1e6, W_cash=1.0):
    # Compute net changes (effects) on each client due to the selected settlements.
    selected = np.asarray(individual, dtype=bool)
    net_cash = np.zeros_like(client_cash)
    net_token = np.zeros_like(client_token)

    # Buyers lose cash and gain tokens; sellers do the opposite.
    np.add.at(net_cash, settlement_buyer[selected], -settlement_cash[selected])
    np.add.at(net_cash, settlement_seller[selected], settlement_cash[selected])
    np.add.at(net_token, settlement_buyer[selected], settlement_token[selected])
    np.add.at(net_token, settlement_seller[selected], -settlement_token[selected])

    # Final balances after applying the selected settlements:
    final_cash = client_cash + net_cash
    final_token = client_token + net_token

    # Compute total infeasibility (if any balance is negative)
    penalty = np.sum(np.maximum(0, -final_cash) + np.maximum(0, -final_token))

    # Compute the “raw” benefit (e.g., number of transactions and total cash moved)
    n_selected = selected.sum()
    total_cash_moved = settlement_cash[selected].sum()
    raw_fitness = n_selected * W_count + total_cash_moved * W_cash

    # Subtract a huge penalty if there is any violation.
    fitness = raw_fitness - 1e7 * penalty

    return (fitness,)


# ----------------------------
# Vectorized local refinement function
# ----------------------------
def local_refinement_vectorized(individual, settlements, client_cash, client_token, settlement_cash, settlement_token,
                                settlement_buyer, settlement_seller):
    current = individual[:]  # Copy current solution
    n = len(current)
    improved = True
    # Sort indices by the cashAmount of each settlement (low-cost first)
    indices = sorted(range(n), key=lambda i: settlements[i]["cashAmount"])
    while improved:
        improved = False
        for i in indices:
            if current[i] == 0:
                candidate = current[:]
                candidate[i] = 1
                sel = np.array(candidate, dtype=bool)
                net_cash = np.zeros_like(client_cash)
                net_token = np.zeros_like(client_token)
                np.add.at(net_cash, settlement_buyer[sel], -settlement_cash[sel])
                np.add.at(net_cash, settlement_seller[sel], settlement_cash[sel])
                np.add.at(net_token, settlement_buyer[sel], settlement_token[sel])
                np.add.at(net_token, settlement_seller[sel], -settlement_token[sel])
                final_cash = client_cash + net_cash
                final_token = client_token + net_token
                if (final_cash < 0).any() or (final_token < 0).any():
                    continue
                current = candidate
                improved = True
                break
    return current


# ----------------------------
# GA using DEAP with binary representation (with selective repair)
# ----------------------------
def run_ga(settlements, balances, ngen=100, pop_size=100, mut_rate=0.25, cx_rate=0.7, repair_rate=0.5):
    # Preprocess data into NumPy arrays.
    client_cash, client_token, settlement_cash, settlement_token, settlement_buyer, settlement_seller, clients = preprocess_data(
        balances, settlements)

    # Configure DEAP.
    if not hasattr(creator, "FitnessMax"):
        creator.create("FitnessMax", base.Fitness, weights=(1.0,))
    if not hasattr(creator, "Individual"):
        creator.create("Individual", list, fitness=creator.FitnessMax)

    toolbox = base.Toolbox()
    n = len(settlements)
    toolbox.register("attr_bool", random.randint, 0, 1)
    toolbox.register("individual", tools.initRepeat, creator.Individual, toolbox.attr_bool, n=n)
    toolbox.register("population", tools.initRepeat, list, toolbox.individual)

    # Register the evaluation function with the preprocessed arrays.
    toolbox.register("evaluate", partial(evaluate_individual_vectorized,
                                         client_cash=client_cash,
                                         client_token=client_token,
                                         settlement_cash=settlement_cash,
                                         settlement_token=settlement_token,
                                         settlement_buyer=settlement_buyer,
                                         settlement_seller=settlement_seller))

    toolbox.register("mate", tools.cxTwoPoint)
    toolbox.register("mutate", tools.mutFlipBit, indpb=0.05)
    toolbox.register("select", tools.selTournament, tournsize=3)

    pool = multiprocessing.Pool(processes=4)
    toolbox.register("map", pool.map)

    pop = toolbox.population(n=pop_size)

    # Inject two heuristic individuals.
    median_cash = np.median(settlement_cash)
    heuristic1 = creator.Individual([1 if amt <= median_cash else 0 for amt in settlement_cash])
    pop[0] = heuristic1
    threshold = np.percentile(settlement_cash, 40)
    heuristic2 = creator.Individual([1 if amt < threshold else 0 for amt in settlement_cash])
    pop[1] = heuristic2

    best = None
    for gen in range(ngen):
        offspring = toolbox.select(pop, len(pop))
        offspring = list(map(toolbox.clone, offspring))
        # Crossover
        for child1, child2 in zip(offspring[::2], offspring[1::2]):
            if random.random() < cx_rate:
                toolbox.mate(child1, child2)
                del child1.fitness.values
                del child2.fitness.values
        # Mutation
        for mutant in offspring:
            if random.random() < mut_rate:
                toolbox.mutate(mutant)
                del mutant.fitness.values

        # Apply repair only if the individual is not feasible and with probability repair_rate.
        for ind in offspring:
            if not is_feasible(ind, client_cash, client_token, settlement_cash, settlement_token,
                               settlement_buyer, settlement_seller):
                if random.random() < repair_rate:
                    repaired = repair_individual(ind, client_cash, client_token,
                                                 settlement_cash, settlement_token,
                                                 settlement_buyer, settlement_seller)
                    ind[:] = repaired

        invalid_ind = [ind for ind in offspring if not ind.fitness.valid]
        fitnesses = toolbox.map(toolbox.evaluate, invalid_ind)
        for ind, fit in zip(invalid_ind, fitnesses):
            ind.fitness.values = fit
        pop[:] = offspring
        best = tools.selBest(pop, 1)[0]
        if gen % 10 == 0 or gen == ngen - 1:
            sel_settlements = [s for bit, s in zip(best, settlements) if bit == 1]
            n_sel = len(sel_settlements)
            total_cash = sum(s["cashAmount"] for s in sel_settlements)
            print(
                f"Generation {gen}: Best fitness = {best.fitness.values[0]:.2f}, Tx = {n_sel}, Cash = {total_cash:.2f}")
    best_individual = best
    pool.close()
    pool.join()

    # Also return the local refinement function bound with the preprocessed arrays.
    local_refine_fn = partial(local_refinement_vectorized,
                              settlements=settlements,
                              client_cash=client_cash,
                              client_token=client_token,
                              settlement_cash=settlement_cash,
                              settlement_token=settlement_token,
                              settlement_buyer=settlement_buyer,
                              settlement_seller=settlement_seller)
    return best_individual, settlements, local_refine_fn


# ----------------------------
# Optuna objective function for hyperparameter optimization
# ----------------------------
def objective(trial):
    pop_size = trial.suggest_int("pop_size", 80, 150)
    ngen = trial.suggest_int("ngen", 60, 100)
    mut_rate = trial.suggest_float("mut_rate", 0.2, 0.3)
    cx_rate = trial.suggest_float("cx_rate", 0.7, 0.8)
    repair_rate = trial.suggest_float("repair_rate", 0.4, 0.6)
    global settlements, balances  # Use global variables loaded in main()
    best, ga_settlements, local_refine_fn = run_ga(settlements, balances,
                                                   ngen=ngen,
                                                   pop_size=pop_size,
                                                   mut_rate=mut_rate,
                                                   cx_rate=cx_rate,
                                                   repair_rate=repair_rate, )
    selected = [s for bit, s in zip(best, ga_settlements) if bit == 1]
    num_transactions = len(selected)
    total_cash = sum(s["cashAmount"] for s in selected)
    score = num_transactions * 1e6 + total_cash
    return score


# ----------------------------
# Main function
# ----------------------------
def main():
    start_time = time.time()
    global balances, settlements
    balances, settlements = load_data("data.json")
    print(f"Loaded {len(settlements)} settlements and {len(balances)} clients.")

    study = optuna.create_study(direction="maximize")
    study.optimize(objective, n_trials=5, n_jobs=1)
    print("Best hyperparameters:", study.best_params)
    print("Best score:", study.best_value)
    best_params = study.best_params

    best, ga_settlements, local_refine_fn = run_ga(
        settlements, balances,
        ngen=best_params["ngen"],
        pop_size=best_params["pop_size"],
        mut_rate=best_params["mut_rate"],
        cx_rate=best_params["cx_rate"]
    )
    # Apply local refinement.
    refined_solution = local_refine_fn(best)
    selected = [s for bit, s in zip(refined_solution, ga_settlements) if bit == 1]
    output_ids = [s["id"] for s in selected]

    # Write output file.
    with open("output.json", "w") as f:
        json.dump({"output": output_ids}, f, indent=2)
    print("\nOutput written to 'output.json'")

    n_sel = len(selected)
    total_cash = sum(s["cashAmount"] for s in selected)
    total_token = sum(s["tokenAmount"] for s in selected)
    print("\nFinal processed settlements (after local refinement):")
    for s in selected:
        print(f"Settlement {s['id']}: Buyer = {s['buyer']}, Seller = {s['seller']}, "
              f"Cash = {s['cashAmount']}, Token = {s['tokenAmount']}")
    print(f"\nTotal processed settlements: {n_sel}")
    print(f"Total cash moved: {total_cash}")
    print(f"Total token moved: {total_token}")
    print(f"Processing time: {time.time() - start_time:.2f} seconds")


if __name__ == '__main__':
    main()
