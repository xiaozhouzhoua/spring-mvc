package com.spring.springmvc.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutor;

import java.util.concurrent.Executor;

/**
 * 自定义线程池-覆盖默认的线程池
 */
@Configuration
@EnableAsync
public class AsyncConfig extends AsyncConfigurerSupport {
    @Bean("asyncExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(3);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("AsyncMvc线程-");
        executor.initialize();
        // 包裹一层DelegatingSecurityContextExecutor让异步线程也可以拿到安全上下文
        return new DelegatingSecurityContextExecutor(executor);
    }

    @Override
    public Executor getAsyncExecutor() {
        return asyncExecutor();
    }
}
