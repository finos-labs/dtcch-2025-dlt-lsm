package io.builders.demo.blockchain.configuration

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
    entityManagerFactoryRef = 'blockchainEntityManagerFactory',
    transactionManagerRef = 'blockchainTransactionManager',
    basePackages = ['io.builders.demo.blockchain']
)
class BlockchainJpaConfiguration {

    @Bean
    LocalContainerEntityManagerFactoryBean blockchainEntityManagerFactory(
        EntityManagerFactoryBuilder builder,
        @Qualifier('blockchainDataSource') DataSource dataSource
    ) {
        builder
            .dataSource(dataSource)
            .packages('io.builders.demo.blockchain')
            .persistenceUnit('blockchain')
            .properties([
                'hibernate.physical_naming_strategy':
                    'org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy'
            ])
            .build()
    }

    @Bean
    PlatformTransactionManager blockchainTransactionManager(
        @Qualifier('blockchainEntityManagerFactory') EntityManagerFactory emf
    ) {
        new JpaTransactionManager(emf)
    }

    @Bean
    JdbcTemplate blockchainJdbcTemplate(@Qualifier('blockchainDataSource') DataSource dataSource) {
        new JdbcTemplate(dataSource)
    }

}
