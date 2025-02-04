package io.builders.demo.surikata.infrastructure.configuration

import io.builders.demo.core.event.DltEvent
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(value = 'surikata.transaction')
class SurikataTransactionMapperConfiguration {

    Map<String, Class<? extends DltEvent>> mappers = [:]

}
