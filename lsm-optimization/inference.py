import json
import requests
import run_ga


def model_fn(model_dir):
    return {}


def input_fn(request_body, request_content_type):
    if request_content_type == 'application/json':
        data = json.loads(request_body)
        return data
    else:
        raise ValueError("content_type not supported: " + request_content_type)


def predict_fn(input_data, model):
    balances = input_data["balances"]
    settlements = input_data["settlements"]
    callback_url = input_data.get("callbackUrl")

    results = run_ga.execute_lsm(balances, settlements)

    if callback_url:
        try:
            r = requests.post(callback_url, json=results, timeout=30)
            print(f"Callback status code: {r.status_code}")
        except Exception as e:
            print(f"Error en callback: {e}")

    return results


def output_fn(prediction, content_type):
    return json.dumps(prediction)
