package io.builders.demo.dtcc.infrastructure.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@ConfigurationProperties('dtcc')
@Configuration
class DtccConfigurationProperties {

    String defaultAddress
    String privateKey
    String cashToken
    String securityToken
    String omnibusAccount
}
