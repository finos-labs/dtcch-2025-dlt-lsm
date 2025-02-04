package io.builders.demo.api.infrastructure.configuration

import liquibase.integration.spring.SpringLiquibase
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import javax.sql.DataSource

@Configuration
class DtccLiquibaseConfiguration {

    @Bean
    SpringLiquibase dtccLiquibase(@Qualifier('dtccDataSource') DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase()
        liquibase.dataSource = dataSource
        liquibase.changeLog = 'classpath:db/dtcc/db.changelog-master.yml'
        liquibase
    }

}
