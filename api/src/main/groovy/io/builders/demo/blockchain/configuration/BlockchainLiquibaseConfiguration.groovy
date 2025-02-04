package io.builders.demo.blockchain.configuration

import liquibase.integration.spring.SpringLiquibase
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import javax.sql.DataSource

@Configuration
class BlockchainLiquibaseConfiguration {

    @Bean
    SpringLiquibase blockchainLiquibase(@Qualifier('blockchainDataSource') DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase()
        liquibase.dataSource = dataSource
        liquibase.changeLog = 'classpath:db/blockchain/db.changelog-master.yml'
        liquibase
    }

}
