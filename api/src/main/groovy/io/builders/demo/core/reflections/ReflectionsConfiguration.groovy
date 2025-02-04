package io.builders.demo.core.reflections

import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.scanners.TypeAnnotationsScanner
import org.reflections.util.ConfigurationBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ReflectionsConfiguration {

    static final String BASE_PACKAGE = 'io.builders.demo.'

    @Bean
    Reflections reflections() {
        new Reflections(
            new ConfigurationBuilder()
                .forPackages(BASE_PACKAGE)
                .setScanners(new SubTypesScanner(), new TypeAnnotationsScanner())
        )
    }

}
