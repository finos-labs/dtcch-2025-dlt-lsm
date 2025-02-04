package io.builders.demo.dtcc.infrastructure.configuration

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import javax.sql.DataSource

@Configuration
class DtccDataSourceConfiguration {

    @Bean
    @ConfigurationProperties('spring.datasource.dtcc')
    DataSourceProperties dtccDataSourceProperties() {
        new DataSourceProperties()
    }

    @Bean
    @ConfigurationProperties('spring.datasource.dtcc.hikari')
    HikariConfig dtccHikariConfig() {
        new HikariConfig()
    }

    @Bean
    DataSource dtccDataSource(
        @Qualifier('dtccDataSourceProperties') DataSourceProperties dataSourceProperties,
        @Qualifier('dtccHikariConfig') HikariConfig hikariConfig
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
