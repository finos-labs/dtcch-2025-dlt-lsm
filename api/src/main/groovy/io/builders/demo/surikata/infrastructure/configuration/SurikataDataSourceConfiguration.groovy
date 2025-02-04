package io.builders.demo.surikata.infrastructure.configuration

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import javax.sql.DataSource

@Configuration
class SurikataDataSourceConfiguration {

    @Bean
    @ConfigurationProperties('spring.datasource.surikata')
    DataSourceProperties surikataDataSourceProperties() {
        new DataSourceProperties()
    }

    @Bean
    @ConfigurationProperties('spring.datasource.surikata.hikari')
    HikariConfig surikataHikariConfig() {
        new HikariConfig()
    }

    @Bean
    DataSource surikataDataSource(
        @Qualifier('surikataDataSourceProperties') DataSourceProperties dataSourceProperties,
        @Qualifier('surikataHikariConfig') HikariConfig hikariConfig
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
