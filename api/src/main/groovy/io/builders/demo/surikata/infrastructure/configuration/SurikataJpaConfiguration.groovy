package io.builders.demo.surikata.infrastructure.configuration

import jakarta.persistence.EntityManagerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager

import javax.sql.DataSource

@Configuration
@EnableJpaRepositories(
    entityManagerFactoryRef = 'surikataEntityManagerFactory',
    transactionManagerRef = 'surikataTransactionManager',
    basePackages = ['io.builders.demo.surikata']
)
class SurikataJpaConfiguration {

    @Bean
    LocalContainerEntityManagerFactoryBean surikataEntityManagerFactory(
        EntityManagerFactoryBuilder builder,
        @Qualifier('surikataDataSource') DataSource dataSource
    ) {
        builder
            .dataSource(dataSource)
            .packages('io.builders.demo.surikata')
            .persistenceUnit('surikata')
            .properties([
                'hibernate.physical_naming_strategy':
                    'org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy'
            ])
            .build()
    }

    @Bean
    PlatformTransactionManager surikataTransactionManager(
        @Qualifier('surikataEntityManagerFactory') EntityManagerFactory emf
    ) {
        new JpaTransactionManager(emf)
    }

    @Bean
    JdbcTemplate surikataJdbcTemplate(@Qualifier('surikataDataSource') DataSource dataSource) {
        new JdbcTemplate(dataSource)
    }

}
