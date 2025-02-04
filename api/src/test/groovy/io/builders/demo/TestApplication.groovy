package io.builders.demo

import net.consensys.eventeum.annotation.EnableEventeum
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.autoconfigure.pulsar.PulsarAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType

@SpringBootApplication(
    exclude = [
        RabbitAutoConfiguration,
        PulsarAutoConfiguration,
        KafkaAutoConfiguration,
        MongoAutoConfiguration,
        MongoRepositoriesAutoConfiguration
    ]
)
@EnableConfigurationProperties
@ComponentScan(
    basePackages = ['io.builders.demo'],
    excludeFilters = [
        @ComponentScan.Filter(
            type = FilterType.ANNOTATION,
            classes = [EnableEventeum]
        )
    ]
)
class TestApplication {

    static void main(String[] args) {
        SpringApplication.run(TestApplication, args)
    }

}
