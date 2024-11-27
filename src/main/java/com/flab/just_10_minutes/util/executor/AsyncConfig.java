package com.flab.just_10_minutes.util.executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    public static final String EVENT_HANDLER_TASK_EXECUTOR = "EVENT_HANDLER_TASK_EXECUTOR";

    @Bean(name ="EVENT_HANDLER_TASK_EXECUTOR")
    public Executor eventHandlerTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("EventHandler-");
        executor.initialize();
        return executor;
    }
}
