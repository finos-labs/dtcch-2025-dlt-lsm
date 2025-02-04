package io.builders.demo.api.infrastructure.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@ConfigurationProperties('dtcc')
@Configuration
class DtccConfigurationProperties {

    String defaultAddress
    String privateKey

}
