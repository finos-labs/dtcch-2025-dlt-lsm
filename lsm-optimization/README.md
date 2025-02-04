# LSM Optimization using Genetic Algorithm

This project implements a **hybrid optimization approach** for the **Liquidity Saving Mechanism (LSM)** using a **Binary
Genetic Algorithm (GA)** combined with **hyperparameter tuning via Optuna** and a **local refinement strategy** to
select a feasible subset of settlements.

## Features

- Uses **DEAP** for evolutionary computation (Genetic Algorithm).
- **Vectorized evaluation** for performance improvements.
- **Optuna** for hyperparameter tuning (population size, mutation/crossover rates, generations).
- **Local refinement step** to ensure feasibility of selected settlements.
- Supports **multiprocessing** for faster evaluations.

## Project Structure

```
lsm-optimization/
│── run_genetic.py          # Main script to run the GA optimization
│── validate_results.py     # Script to validate the output
│── requirements.txt        # Python dependencies
│── README.md               # Project documentation
│── data.json               # Input data containing balances and settlements
│── output.json             # Output file containing selected settlement IDs
```

## Installation

### 1. Create a Virtual Environment (Optional but Recommended)

```bash
python3 -m venv venv
source venv/bin/activate  # On macOS/Linux
venv\Scripts\activate    # On Windows
```

### 2. Install Dependencies

```bash
pip install -r requirements.txt
```

## Running the Optimization

To run the optimization using the genetic algorithm, execute:

```bash
python run_genetic.py
```

This will:

1. Load **balances** and **settlements** from `data.json`.
2. Optimize settlement selection using a **binary genetic algorithm**.
3. Tune hyperparameters with **Optuna**.
4. Store the selected settlements in `output.json`.

## Validating the Results

To validate the output:

```bash
python validate_results.py --data data.json --selected output.json
```

If no arguments are provided, the script will default to using `data.json` and `output.json`.

## Input Data Format (`data.json`)

The input file should be a **JSON object** containing:

```json
{
  "balances": [
    {
      "client": "A",
      "cashAmount": 100,
      "tokenAmount": 50
    },
    {
      "client": "B",
      "cashAmount": 200,
      "tokenAmount": 30
    }
  ],
  "settlements": [
    {
      "id": 1,
      "buyer": "A",
      "seller": "B",
      "cashAmount": 20,
      "tokenAmount": 10
    },
    {
      "id": 2,
      "buyer": "B",
      "seller": "A",
      "cashAmount": 50,
      "tokenAmount": 5
    }
  ]
}
```

## Output Format (`output.json`)

The output file will contain the **IDs of the selected settlements**:

```json
{
  "output": [
    1,
    2
  ]
}
```

## Performance Considerations

- The algorithm is **vectorized** for performance efficiency.
- Uses **multiprocessing** for faster evaluation.
- Applies **heuristic-based initialization** to inject structured solutions.

