package io.builders.demo.surikata.infrastructure.configuration

import liquibase.integration.spring.SpringLiquibase
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import javax.sql.DataSource

@Configuration
class SurikataLiquibaseConfiguration {

    @Bean
    SpringLiquibase surikataLiquibase(@Qualifier('surikataDataSource') DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase()
        liquibase.dataSource = dataSource
        liquibase.changeLog = 'classpath:db/surikata/db.changelog-master.yml'
        liquibase
    }

}
