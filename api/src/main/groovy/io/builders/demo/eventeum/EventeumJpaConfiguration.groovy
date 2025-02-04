package io.builders.demo.eventeum

import jakarta.persistence.EntityManagerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
class EventeumJpaConfiguration {

    @Bean
    @Primary
    LocalContainerEntityManagerFactoryBean entityManagerFactory(
        EntityManagerFactoryBuilder builder,
        @Qualifier('eventeumDataSource') DataSource dataSource
    ) {
        builder
            .dataSource(dataSource)
            .packages('net.consensys.eventeum')
            .persistenceUnit('eventeum')
            .properties([
                'hibernate.physical_naming_strategy':
                    'org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy'
            ])
            .build()
    }

    @Bean
    @Primary
    PlatformTransactionManager transactionManager(@Qualifier('entityManagerFactory') EntityManagerFactory emf) {
        new JpaTransactionManager(emf)
    }

    @Bean
    @Primary
    JdbcTemplate jdbcTemplate(@Qualifier('eventeumDataSource') DataSource dataSource) {
        new JdbcTemplate(dataSource)
    }

}
