package com.xpressbees.config;

import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;

@Configuration
public class AppConfig {
  @Bean(name = "orderThreadPool")
  public Executor threadPoolTaskExecutor(OrderConfig config) {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(config.getMaxThreads());
    executor.setMaxPoolSize(config.getMaxThreads() * 2);
    executor.setQueueCapacity(config.getQueueCapacity());
    executor.setThreadNamePrefix("OrderProcessor-");
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    executor.initialize();
    return executor;
  }
}


