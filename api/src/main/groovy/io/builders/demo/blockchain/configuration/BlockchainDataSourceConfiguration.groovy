package io.builders.demo.blockchain.configuration

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import javax.sql.DataSource

@Configuration
class BlockchainDataSourceConfiguration {

    @Bean
    @ConfigurationProperties('spring.datasource.blockchain')
    DataSourceProperties blockchainDataSourceProperties() {
        new DataSourceProperties()
    }

    @Bean
    @ConfigurationProperties('spring.datasource.blockchain.hikari')
    HikariConfig blockchainHikariConfig() {
        new HikariConfig()
    }

    @Bean
    DataSource blockchainDataSource(
        @Qualifier('blockchainDataSourceProperties') DataSourceProperties dataSourceProperties,
        @Qualifier('blockchainHikariConfig') HikariConfig hikariConfig
    ) {
        HikariDataSource dataSource = dataSourceProperties
            .initializeDataSourceBuilder()
            .type(HikariDataSource)
            .build()

        dataSource.poolName = hikariConfig.poolName
        dataSource.maximumPoolSize = hikariConfig.maximumPoolSize
        dataSource.minimumIdle = hikariConfig.minimumIdle
        dataSource.connectionTimeout = hikariConfig.connectionTimeout

        dataSource
    }

}
