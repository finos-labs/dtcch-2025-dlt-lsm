import os
import time
import boto3
from dotenv import load_dotenv

# 🔄 Load environment variables from the .env file
load_dotenv()

# 🔍 Retrieve environment variables
aws_region = os.getenv("AWS_REGION")
ecr_image_uri = os.getenv("ECR_IMAGE_URI")
s3_bucket = os.getenv("S3_BUCKET")
sagemaker_role = os.getenv("SAGEMAKER_ROLE")
endpoint_name = os.getenv("ENDPOINT_NAME")
endpoint_config_name = os.getenv("ENDPOINT_CONFIG_NAME")
model_name = os.getenv("MODEL_NAME")

# 🛠️ Create the SageMaker client using the configured region
sagemaker_client = boto3.client("sagemaker", region_name=aws_region)

# ⚠️ If an endpoint already exists, delete it to be able to create a new one
first_time = False
if not first_time:
    try:
        sagemaker_client.delete_endpoint(EndpointName=endpoint_name)
        print(f"🗑️ Deleted endpoint: {endpoint_name}")
    except Exception as e:
        print(f"❌ Could not delete endpoint ({endpoint_name}): {e}")

    try:
        sagemaker_client.delete_endpoint_config(EndpointConfigName=endpoint_config_name)
        print(f"🗑️ Deleted endpoint configuration: {endpoint_config_name}")
    except Exception as e:
        print(f"❌ Could not delete endpoint configuration ({endpoint_config_name}): {e}")

    try:
        sagemaker_client.delete_model(ModelName=model_name)
        print(f"🗑️ Deleted model: {model_name}")
    except Exception as e:
        print(f"❌ Could not delete model ({model_name}): {e}")

# 🚀 Create the model in SageMaker
sagemaker_client.create_model(
    ModelName=model_name,
    PrimaryContainer={
        "Image": ecr_image_uri,
        "Environment": {"SAGEMAKER_MODEL_SERVER": "gunicorn", "GUNICORN_TIMEOUT": "1200"}
    },
    ExecutionRoleArn=sagemaker_role,
)
print(f"✅ Model {model_name} created successfully.")

# 📝 Create the endpoint configuration, defining the production variant and asynchronous configuration
sagemaker_client.create_endpoint_config(
    EndpointConfigName=endpoint_config_name,
    ProductionVariants=[
        {
            "VariantName": "GA-LSM-Variant",
            "ModelName": model_name,
            "InstanceType": "ml.m5.4xlarge",
            "InitialInstanceCount": 1
        }
    ],
    AsyncInferenceConfig={
        "OutputConfig": {
            "S3OutputPath": f"s3://{s3_bucket}/async-inference-results/"
        }
    }
)
print(f"✅ Endpoint configuration {endpoint_config_name} created successfully.")

# 🔄 Create the asynchronous endpoint
sagemaker_client.create_endpoint(
    EndpointName=endpoint_name,
    EndpointConfigName=endpoint_config_name
)
print("🚀 Creating asynchronous inference endpoint...")

# ⏳ Wait until the endpoint is in service
while True:
    response = sagemaker_client.describe_endpoint(EndpointName=endpoint_name)
    status = response["EndpointStatus"]
    print("📊 Endpoint status:", status)

    if status == "InService":
        print("🎉 Endpoint is ready!")
        break
    elif status == "Failed":
        print("⚠️ Error:", response)
        break

    time.sleep(30)
