package com.hitecart.deliverysystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean
    public TaskExecutor taskExecutor() {
        // This creates a standard task executor.
        SimpleAsyncTaskExecutor delegate = new SimpleAsyncTaskExecutor();

        // This wraps the executor to ensure the security context is passed to @Async methods.
        return new DelegatingSecurityContextAsyncTaskExecutor(delegate);
    }
}
