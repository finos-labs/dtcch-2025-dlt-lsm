import json
import random
import uuid
import argparse


def generate_data(num_clients=5, num_settlements=10, output_file="data.json"):
    clients = [str(uuid.uuid4()) for _ in range(num_clients)]  # Generate UUIDs for clients
    balances = [
        {
            "client": client,
            "cashAmount": random.randint(50, 500),
            "tokenAmount": random.randint(20, 200)
        }
        for client in clients
    ]

    settlements = []
    for _ in range(num_settlements):
        buyer, seller = random.sample(clients, 2)  # Ensure buyer and seller are different
        settlements.append({
            "id": str(uuid.uuid4()),  # Generate UUID for settlements
            "buyer": buyer,
            "seller": seller,
            "cashAmount": random.randint(10, 100),
            "tokenAmount": random.randint(5, 50)
        })

    data = {"balances": balances, "settlements": settlements}

    with open(output_file, "w") as f:
        json.dump(data, f, indent=2)

    print(f"Generated {output_file} with {num_clients} clients and {num_settlements} settlements.")


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Generate random balances and settlements JSON.")
    parser.add_argument("--clients", type=int, default=5, help="Number of clients to generate")
    parser.add_argument("--settlements", type=int, default=10, help="Number of settlements to generate")
    parser.add_argument("--output", type=str, default="data.json", help="Output JSON file")
    args = parser.parse_args()

    generate_data(args.clients, args.settlements, args.output)