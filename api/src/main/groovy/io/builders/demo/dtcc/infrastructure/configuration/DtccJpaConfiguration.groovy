package io.builders.demo.dtcc.infrastructure.configuration

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
    entityManagerFactoryRef = 'dtccEntityManagerFactory',
    transactionManagerRef = 'dtccTransactionManager',
    basePackages = ['io.builders.demo.dtcc']
)
class DtccJpaConfiguration {

    @Bean
    LocalContainerEntityManagerFactoryBean dtccEntityManagerFactory(
        EntityManagerFactoryBuilder builder,
        @Qualifier('dtccDataSource') DataSource dataSource
    ) {
        builder
            .dataSource(dataSource)
            .packages('io.builders.demo.dtcc')
            .persistenceUnit('dtcc')
            .properties([
                'hibernate.physical_naming_strategy':
                    'org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy'
            ])
            .build()
    }

    @Bean
    PlatformTransactionManager dtccTransactionManager(
        @Qualifier('dtccEntityManagerFactory') EntityManagerFactory emf
    ) {
        new JpaTransactionManager(emf)
    }

    @Bean
    JdbcTemplate dtccJdbcTemplate(@Qualifier('dtccDataSource') DataSource dataSource) {
        new JdbcTemplate(dataSource)
    }

}
