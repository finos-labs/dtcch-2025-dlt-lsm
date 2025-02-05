import json
import os
import tempfile

import boto3
import requests
from flask import Flask, request, jsonify

import run_ga

app = Flask(__name__)

S3_BUCKET = os.environ.get("S3_BUCKET", "ga-lsm-bucket")
s3_client = boto3.client("s3")


@app.route('/ping', methods=['GET'])
def ping():
    return jsonify(status="ok")


@app.route('/invocations', methods=['POST'])
def invoke():
    try:
        with tempfile.NamedTemporaryFile(delete=False, suffix=".json") as temp_file:
            temp_file.write(request.data)
            temp_filename = temp_file.name

        with open(temp_filename, "r", encoding="utf-8") as f:
            request_json = json.load(f)

        balances = request_json.get("balances")
        settlements = request_json.get("settlements")
        callback_url = request_json.get("callbackUrl")

        if not balances or not settlements or not callback_url:
            return jsonify(error="Missing required fields in JSON"), 400

        balances, settlements = run_ga.preprocess_json(request_json)
        results = run_ga.execute_lsm(balances, settlements)

        try:
            response = requests.post(callback_url, json=results, timeout=30)
            print(f"Callback sent, status code: {response.status_code}")
        except Exception as e:
            print(f"Error sending callback: {e}")

        return jsonify(results=results)

    except Exception as e:
        return jsonify(error=str(e)), 500


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8080)
