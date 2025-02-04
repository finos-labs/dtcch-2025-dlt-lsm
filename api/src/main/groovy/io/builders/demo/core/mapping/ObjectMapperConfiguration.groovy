package io.builders.demo.core.mapping

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

import java.time.ZoneId

@Configuration
class ObjectMapperConfiguration {

    @Bean
    @Primary
    ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        builder
            .modulesToInstall(new JavaTimeModule())
            .serializationInclusion(JsonInclude.Include.NON_ABSENT)
            .featuresToDisable(
                SerializationFeature.FAIL_ON_EMPTY_BEANS,
                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
            )
            .featuresToEnable(SerializationFeature.INDENT_OUTPUT)
            .timeZone(TimeZone.getTimeZone(ZoneId.of('UTC')))
            .build()
    }

}
