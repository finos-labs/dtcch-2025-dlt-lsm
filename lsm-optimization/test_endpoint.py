import os
import json
import boto3
import uuid
from dotenv import load_dotenv

# 🔄 Load environment variables from the .env file
load_dotenv()

# 🔍 Retrieve environment variables (with defaults if not defined)
aws_region = os.getenv("AWS_REGION", "us-west-2")
s3_bucket = os.getenv("S3_BUCKET", "ga-lsm-bucket")
endpoint_name = os.getenv("ENDPOINT_NAME", "GA-LSM-Async-Endpoint")
input_file = os.getenv("INPUT_FILE", "data.json")

# 🛠️ Create the S3 and SageMaker Runtime clients using the configured region
s3_client = boto3.client("s3", region_name=aws_region)
sagemaker_runtime = boto3.client("sagemaker-runtime", region_name=aws_region)

# Generate a unique key for the input JSON file on S3
input_key = f"async-inference-inputs/{uuid.uuid4()}.json"

# 📄 Read the JSON payload from the specified input file
with open(input_file, "r") as f:
    payload = json.load(f)

# 🚀 Upload the JSON payload to S3
s3_client.put_object(Bucket=s3_bucket, Key=input_key, Body=json.dumps(payload))
s3_input_uri = f"s3://{s3_bucket}/{input_key}"
print("✅ JSON uploaded to S3 in:", s3_input_uri)

# 🔄 Invoke the asynchronous SageMaker endpoint using the S3 URI as input
response = sagemaker_runtime.invoke_endpoint_async(
    EndpointName=endpoint_name,
    InputLocation=s3_input_uri
)

# 📌 Retrieve and print the output location where the results will be stored
output_location = response["OutputLocation"]
print("📌 SageMaker is processing your request. When it finishes, the results will be in:", output_location)
