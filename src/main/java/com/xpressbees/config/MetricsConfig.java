package com.xpressbees.config;

import com.xpressbees.repository.OrderRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

  @Bean
  public MeterRegistryCustomizer<MeterRegistry> orderMetrics(OrderRepository repository) {
    return registry -> {
      Gauge.builder("orders.status.pending", repository::countByDeliveryStatusPending)
          .description("Current pending orders count")
          .register(registry);

      Gauge.builder("orders.status.in_progress", repository::countByDeliveryStatusInProgress)
          .description("Current in-progress orders count")
          .register(registry);

      Gauge.builder("orders.status.delivered", repository::countByDeliveryStatusDelivered)
          .description("Current delivered orders count")
          .register(registry);
    };
  }
}