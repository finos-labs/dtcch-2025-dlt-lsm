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

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(awsRegion).build()
        AmazonSageMakerRuntime sagemakerRuntime = AmazonSageMakerRuntimeClientBuilder.standard().withRegion(awsRegion).build()

        String inputKey = "async-inference-inputs/${UUID.randomUUID()}.json"
        String payload = toJson(smRequest, callbackUrl)

        s3Client.putObject(s3Bucket, inputKey, payload)
        String s3InputUri = "s3://${s3Bucket}/${inputKey}"
        println("✅ JSON uploaded to S3 in: ${s3InputUri}")

        InvokeEndpointAsyncRequest request = new InvokeEndpointAsyncRequest()
            .withEndpointName(endpointName)
            .withInputLocation(s3InputUri)

        sagemakerRuntime.invokeEndpointAsync(request)

    }

    private String toJson(SMRequest request, String callbackUrl) {
        Map<String, Object> jsonMap = [
            balances   : request.balances.collect {
                [
                    client     : it.client,
                    cashAmount : it.cashAmount.toString(),
                    tokenAmount: it.tokenAmount.toString()
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
        return JsonOutput.toJson(jsonMap)
    }
}
