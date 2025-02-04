package io.builders.demo.eventeum

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

import javax.sql.DataSource

@Configuration
class EventeumDataSourceConfiguration {

    @Bean
    @ConfigurationProperties('spring.datasource.eventeum')
    DataSourceProperties eventeumDataSourceProperties() {
        new DataSourceProperties()
    }

    @Bean
    @ConfigurationProperties('spring.datasource.eventeum.hikari')
    HikariConfig eventeumHikariConfig() {
        new HikariConfig()
    }

    @Bean
    @Primary
    DataSource eventeumDataSource(
        @Qualifier('eventeumDataSourceProperties') DataSourceProperties dataSourceProperties,
        @Qualifier('eventeumHikariConfig') HikariConfig hikariConfig
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
