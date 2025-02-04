package io.builders.demo.core.mapping

import org.modelmapper.Converter
import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class ModelMapperConfiguration {

    @Bean
    @Primary
    ModelMapper modelMapper(List<Converter> converters) {
        ModelMapper modelMapper = new MetaClassAwareModelMapper()
        modelMapper.configuration.matchingStrategy = MatchingStrategies.LOOSE
        converters.each { Converter converter -> modelMapper.addConverter(converter) }
        modelMapper
    }

    @Bean
    ModelMapper strictModelMapper(List<Converter> converters) {
        ModelMapper modelMapper = new MetaClassAwareModelMapper()
        modelMapper.configuration.matchingStrategy = MatchingStrategies.STRICT
        converters.each { Converter converter -> modelMapper.addConverter(converter) }
        modelMapper
    }

}
