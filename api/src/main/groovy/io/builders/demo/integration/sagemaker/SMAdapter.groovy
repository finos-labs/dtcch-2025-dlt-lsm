    package io.builders.demo.integration.sagemaker

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.sagemakerruntime.AmazonSageMakerRuntime
import com.amazonaws.services.sagemakerruntime.AmazonSageMakerRuntimeClientBuilder
import com.amazonaws.services.sagemakerruntime.model.InvokeEndpointAsyncRequest
import groovy.json.JsonOutput
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Component

@Component
@Slf4j
@SuppressWarnings(['NoDef', 'VariableTypeRequired', 'CatchException', 'MethodReturnTypeRequired', 'LineLength'])
class SMAdapter implements SMPort {

    @Override
    void makeSMRequest(SMRequest smRequest) {
        String awsRegion = "us-west-2"
        String s3Bucket = "ga-lsm-bucket"
        String endpointName = "GA-LSM-Async-Endpoint"
        String callbackUrl = "https://webhook.site/235bd5f6-5317-4101-9b7e-98f0216747a2"

        // 🛠️ Create the S3 and SageMaker Runtime clients using the configured region
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(awsRegion).build()
        AmazonSageMakerRuntime sagemakerRuntime = AmazonSageMakerRuntimeClientBuilder.standard().withRegion(awsRegion).build()

        // Generate a unique key for the input JSON file on S3
        String inputKey = "async-inference-inputs/${UUID.randomUUID()}.json"

        // 📄 Read the JSON payload from the specified input file
        String payload = toJson(smRequest, callbackUrl)

        // 🚀 Upload the JSON payload to S3
        s3Client.putObject(s3Bucket, inputKey, JsonOutput.toJson(payload))
        String s3InputUri = "s3://${s3Bucket}/${inputKey}"
        println("✅ JSON uploaded to S3 in: ${s3InputUri}")

        // 🔄 Invoke the asynchronous SageMaker endpoint using the S3 URI as input
        InvokeEndpointAsyncRequest request = new InvokeEndpointAsyncRequest()
            .withEndpointName(endpointName)
            .withInputLocation(s3InputUri)

        sagemakerRuntime.invokeEndpointAsync(request)

    }

    private String toJson(SMRequest request, String callbackUrl) {
        Map<String, Object> jsonMap = [
            balances: request.balances.collect {
                [
                    user        : it.client,
                    cashAmount  : it.cashAmount.toString(),
                    tokenAmount : it.tokenAmount.toString()
                ]
            },
            settlements: request.settlements.collect {
                [
                    id         : it.id,
                    buyer      : it.buyer,
                    seller     : it.seller,
                    cashAmount : it.cashAmount,
                    tokenAmount: it.tokenAmount
                ]
            },
            callbackUrl: callbackUrl
        ]
        return JsonOutput.prettyPrint(JsonOutput.toJson(jsonMap))
    }
}
