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

import argparse
import json
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
    balances = {b["client"]: {"cash": b["cashAmount"], "token": b["tokenAmount"]} for b in data["balances"]}

    # Filter selected settlements
    selected_settlements = [s for s in settlements if s["id"] in selected_ids]

    # Update balances based on selected settlements
    for s in selected_settlements:
        buyer, seller = s["buyer"], s["seller"]
        cash, token = s["cashAmount"], s["tokenAmount"]

        # Buyer: cash is deducted, tokens are added
        balances[buyer]["cash"] -= cash
        balances[buyer]["token"] += token

        # Seller: cash is added, tokens are deducted
        balances[seller]["cash"] += cash
        balances[seller]["token"] -= token

    # Validate balances
    valid = True
    print("\nSummary of resulting balances after applying settlements:")
    print("Total clients:", len(balances))
    print("Total settlements:", len(settlements))
    print(f"Total selected settlements: {len(selected_settlements)}\n")
    for client, balance in balances.items():
        cash, token = balance["cash"], balance["token"]
        print(f"Client '{client}': Final cash = {cash:.3f}, Final tokens = {token:.3f}")

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
    parser.add_argument("--selected", default="output.json",
                        help="JSON file with selected settlement IDs (e.g., output.json)")
    args = parser.parse_args()

    # Load data files
    data = load_json(args.data)
    selected_ids = set(load_json(args.selected)["output"])  # Convert to set for faster lookup

    print("Selected settlement IDs:")
    print(selected_ids, "\n")

    if validate_selection(data, selected_ids):
        print("\nThe settlement selection is VALID: it can be processed without exceeding balances.")
    else:
        print("\nThe settlement selection is NOT valid.")


if __name__ == "__main__":
    main()
