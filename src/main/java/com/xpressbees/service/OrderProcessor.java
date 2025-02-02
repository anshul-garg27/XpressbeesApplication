package com.xpressbees.service;

import com.xpressbees.config.OrderConfig;
import com.xpressbees.enums.DeliveryStatus;
import com.xpressbees.model.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
public class OrderProcessor {
  private final OrderService orderService;
  private final OrderConfig orderConfig;
  private final Random random = new Random();

  public OrderProcessor(OrderService orderService, OrderConfig orderConfig) {
    this.orderService = orderService;
    this.orderConfig = orderConfig;
  }

  @Scheduled(fixedRateString = "${processing.interval:5000}")
  public void processOrderBatch() {
    int batchSize = orderConfig.getBatchSize();
    List<Order> orders = orderService.lockAndProcessPendingOrders(batchSize);
    orders.forEach(this::processSingleOrder);
  }

  private void processSingleOrder(Order order) {
    try {
      updateOrderStatus(order.getOrderId(), DeliveryStatus.IN_PROGRESS);
      simulateDelivery();
      updateOrderStatus(order.getOrderId(), DeliveryStatus.DELIVERED);
    } catch (Exception e) {
      handleOrderFailure(order.getOrderId(), e);
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void updateOrderStatus(Integer orderId, DeliveryStatus status) {
    orderService.updateOrderStatus(orderId, status);
  }

  private void simulateDelivery() throws InterruptedException {
    Thread.sleep(2000 + random.nextInt(3000));
  }

  private void sleepBetweenBatches() {
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  private void handleOrderFailure(Integer orderId, Exception e) {
    // Implement retry logic or dead-letter queue
  }
}