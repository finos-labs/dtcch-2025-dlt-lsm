#!/usr/bin/env python3
"""
Script to verify that the selection of settlements is valid,
meaning that clients have sufficient balances to process
the selected settlements.

The convention is:
  - If a client is a buyer in a settlement:
       • Their "cashAmount" is deducted (i.e., they need to have that cash available)
       • Their "tokenAmount" is increased (they receive tokens)
  - If they are a seller:
       • Their "cashAmount" is increased (they receive cash)
       • Their "tokenAmount" is deducted (they must have tokens available)

The selection is considered valid if, for each client,
the resulting cash and token balances (after applying the settlements)
are non-negative.
"""

import json
import argparse
import sys


def load_json(filename):
    """Loads and returns the content of a JSON file."""
    try:
        with open(filename, "r") as f:
            return json.load(f)
    except FileNotFoundError:
        print(f"Error: File '{filename}' not found.")
        sys.exit(1)
    except json.JSONDecodeError:
        print(f"Error: Invalid JSON format in '{filename}'.")
        sys.exit(1)


def validate_selection(data, selected_ids):
    """
    Checks if the selection of settlements is feasible according to available balances.

    Returns True if the selection is valid, False otherwise.
    """
    settlements = data["settlements"]
    balances = data["balances"]

    # Initialize net effects for each client
    # These represent the cumulative effect of all selected settlements.
    # For a buyer, cash is reduced and tokens are increased.
    # For a seller, cash is increased and tokens are reduced.
    net_effects = {
        client: {
            "cash": data["cashAmount"],
            "token": data["tokenAmount"]
        }
        for client, data in balances.items()
    }

    print(net_effects)

    # Filter selected settlements
    selected_settlements = [s for s in settlements if s["id"] in selected_ids]

    # Update net effects for each settlement
    for s in selected_settlements:
        buyer, seller = s["buyer"], s["seller"]
        cash, token = s["cashAmount"], s["tokenAmount"]

        # Buyer: cash is deducted, tokens are added
        net_effects[buyer]["cash"] -= cash
        print(f"Client {buyer} has {cash} cash left. Now his balance is {net_effects[buyer]['cash']}")
        net_effects[buyer]["token"] += token
        print(f"Client {buyer} has {token} token left. Now his balance is {net_effects[buyer]['token']}")

        # Seller: cash is added, tokens are deducted
        net_effects[seller]["cash"] += cash
        print(f"Client {seller} has {cash} cash left. Now his balance is {net_effects[seller]['cash']}")
        net_effects[seller]["token"] -= token
        print(f"Client {seller} has {token} token left. Now his balance is {net_effects[seller]['token']}")

    # Compare resulting balances (initial balance + net effect) with zero
    valid = True
    print("\nSummary of resulting balances after applying settlements:")
    for client, effect in net_effects.items():
        cash = effect["cash"]
        token = effect["token"]

        print(f"Client '{client}': Final cash = {cash:.3f} (initial {balances[client]["cashAmount"]:.3f}), "
              f"Final tokens = {token:.3f} (initial {balances[client]["tokenAmount"]:.3f}), ")

        if cash < 0:
            print(f"  -> ERROR: Client '{client}' would have a negative cash balance.")
            valid = False
        if token < 0:
            print(f"  -> ERROR: Client '{client}' would have a negative token balance.")
            valid = False

    return valid


def main():
    parser = argparse.ArgumentParser(description="Validates settlement selection feasibility.")
    parser.add_argument("--data", default="data.json", help="JSON file with settlements and balances (e.g., data.json)")
    parser.add_argument("--selected", default="output.json", help="JSON file with selected settlement IDs (e.g., output.json)")
    args = parser.parse_args()

    # Load data files
    data = load_json(args.data)
    selected_ids = load_json(args.selected)["output"]

    print("Selected settlement IDs:")
    print(selected_ids, "\n")

    if validate_selection(data, selected_ids):
        print("\nThe settlement selection is VALID: it can be processed without exceeding balances.")
    else:
        print("\nThe settlement selection is NOT valid.")


if __name__ == "__main__":
    main()
