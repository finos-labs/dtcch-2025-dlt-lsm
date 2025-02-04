package io.builders.demo

import net.consensys.eventeum.annotation.EnableEventeum
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration
import org.springframework.boot.autoconfigure.pulsar.PulsarAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@EnableEventeum
@EnableConfigurationProperties
@SpringBootApplication(
    exclude = [
        RabbitAutoConfiguration,
        PulsarAutoConfiguration,
        KafkaAutoConfiguration,
        MongoRepositoriesAutoConfiguration
    ]
)
class Application {

    static void main(String[] args) {
        SpringApplication.run(Application, args)
    }

}
