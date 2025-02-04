package io.builders.demo.core.task

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties('task-executor')
class TaskExecutorConfigurationProperties {

    Integer corePoolSize = 1
    Integer maxPoolSize = Integer.MAX_VALUE
    Integer queueCapacity = Integer.MAX_VALUE

}
