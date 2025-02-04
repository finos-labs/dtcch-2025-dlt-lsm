package io.builders.demo.eventeum

import liquibase.integration.spring.SpringLiquibase
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

import javax.sql.DataSource

@Configuration
class EventeumLiquibaseConfiguration {

    @Bean
    @Primary
    SpringLiquibase eventeumLiquibase(@Qualifier('eventeumDataSource') DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase()
        liquibase.dataSource = dataSource
        liquibase.changeLog = 'classpath:db/eventeum/db.changelog-master.yml'
        liquibase
    }

}
