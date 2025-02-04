package io.builders.demo.core

import io.builders.demo.TestApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.Network
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.spock.Testcontainers
import org.testcontainers.utility.DockerImageName
import spock.lang.Shared
import spock.lang.Specification

import java.time.Duration
import java.time.Instant

import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j

@Slf4j
@CompileDynamic
@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(classes = TestApplication)
@ContextConfiguration(initializers = Initializer)
abstract class BaseSpecification extends Specification {

    static final String POSTGRES_MAX_CONNECTIONS_ENV_VAR_NAME = 'POSTGRES_MAX_CONNECTIONS'
    static final String POSTGRES_MAX_CONNECTIONS = System.getenv(POSTGRES_MAX_CONNECTIONS_ENV_VAR_NAME) ?: '250'
    static final String POSTGRESQL_USER_PWD = 'demo'
    static final int POSTGRESQL_PORT = 5432

    @Shared
    static Network network = Network.newNetwork()

    @Shared
    static PostgreSQLContainer psqlContainer = postgreSQLImage(network)

    @Autowired(required = false)
    List<TestListener> testListeners

    @Autowired
    DatabaseCleaner databaseCleaner

    static PostgreSQLContainer postgreSQLImage(Network network) {
        return new PostgreSQLContainer<>(
            DockerImageName.parse('postgres:14.1')
                .asCompatibleSubstituteFor('postgres'))
            .withExposedPorts(POSTGRESQL_PORT)
            .withUsername(POSTGRESQL_USER_PWD)
            .withPassword(POSTGRESQL_USER_PWD)
            .withInitScript('db/init.sql')
            .withNetwork(network)
            .withNetworkAliases('postgresql')
            .withEnv(POSTGRES_MAX_CONNECTIONS_ENV_VAR_NAME, POSTGRES_MAX_CONNECTIONS)
    }

    static {
        Instant start = Instant.now()
        psqlContainer.start()

        Initializer.postgresHost = psqlContainer.host
        Initializer.postgresPort = psqlContainer.getMappedPort(POSTGRESQL_PORT)

        System.setProperty('spring.profiles.active', 'test')
        log.info(
            "🐘 Postgres (${POSTGRES_MAX_CONNECTIONS} max-connections) management port: ${psqlContainer.getMappedPort(POSTGRESQL_PORT)}")
        log.info("🐳 TestContainers started in ${Duration.between(start, Instant.now())}")
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        static String postgresHost
        static Integer postgresPort

        @Override
        void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                "EVENTEUM_DATABASE_HOST=${postgresHost}",
                "EVENTEUM_DATABASE_PORT=${postgresPort}",
                "BLOCKCHAIN_DATABASE_HOST=${postgresHost}",
                "BLOCKCHAIN_DATABASE_PORT=${postgresPort}",
                "SURIKATA_DATABASE_HOST=${postgresHost}",
                "SURIKATA_DATABASE_PORT=${postgresPort}",
                "DTCC_DATABASE_HOST=${postgresHost}",
                "DTCC_DATABASE_PORT=${postgresPort}",
                'spring.profiles.active=test'
            ).applyTo(applicationContext)
        }

    }

    void cleanup() {
        databaseCleaner.cleanup()
        testListeners.each { testListener -> testListener.cleanup() }
    }

}
