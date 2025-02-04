package io.builders.demo.core.task

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@Configuration
class TaskExecutorConfiguration {

    @Autowired
    TaskExecutorConfigurationProperties configurationProperties

    @Bean
    ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor()
        executor.corePoolSize = configurationProperties.corePoolSize
        executor.maxPoolSize = configurationProperties.maxPoolSize
        executor.queueCapacity = configurationProperties.queueCapacity
        executor.threadNamePrefix = 'Thread-'
        executor.initialize()
        return executor
    }

}
