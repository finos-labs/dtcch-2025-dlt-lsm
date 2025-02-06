package io.builders.demo.dtcc.infrastructure.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@ConfigurationProperties('sagemaker')
@Configuration
class SagemakerConfigurationProperties {

    String awsRegion
    String s3Bucket
    String endpointName
    String callbackUrl

}
