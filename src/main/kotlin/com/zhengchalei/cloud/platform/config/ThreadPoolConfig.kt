package com.zhengchalei.cloud.platform.config

import com.zhengchalei.cloud.platform.config.security.SecurityContextTaskDecorator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.scheduling.annotation.EnableAsync

class VirtualThreadExecutor : SimpleAsyncTaskExecutor()

@EnableAsync
@Configuration
class ThreadPoolConfig {
    @Bean
    fun businessVirtualThreadExecutor(): VirtualThreadExecutor {
        return virtualThreadExecutor("business")
    }
}

fun virtualThreadExecutor(name: String = ""): VirtualThreadExecutor {
    val virtualThreadExecutor = VirtualThreadExecutor()
    virtualThreadExecutor.setVirtualThreads(true)
    virtualThreadExecutor.setTaskDecorator(SecurityContextTaskDecorator())
    virtualThreadExecutor.threadFactory =
        Thread.ofVirtual().name("virtual-$name-executor", 0).factory()
    virtualThreadExecutor.setTaskTerminationTimeout(5000)
    return virtualThreadExecutor
}
