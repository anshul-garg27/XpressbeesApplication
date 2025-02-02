package com.xpressbees.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "order.processing")
@Data // Lombok
public class OrderConfig {
  private int batchSize = 5;
  private int maxThreads = 10;
  private int queueCapacity = 100;
}