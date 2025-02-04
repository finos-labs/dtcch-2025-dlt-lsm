package io.builders.demo.dtcc.infrastructure.configuration

import io.builders.demo.core.event.DltEvent
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(value = 'dtcc.event')
class DtccEventMapperConfiguration {

    Map<String, Class<? extends DltEvent>> mappers = [:]

}
