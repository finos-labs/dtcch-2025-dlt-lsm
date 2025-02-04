package io.builders.demo.integration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "ia")
@SuppressWarnings(['UnusedPrivateField'])
class IAConfiguration {

    private String url
    private String model
    private String apiKey

}
