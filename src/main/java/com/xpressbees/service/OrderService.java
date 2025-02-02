package com.xpressbees.service;

import com.xpressbees.DTO.TopCustomerDTO;
import com.xpressbees.enums.DeliveryStatus;
import com.xpressbees.model.Order;
import com.xpressbees.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

  private final OrderRepository orderRepository;

  public OrderService(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @Transactional
  public Order createOrder(Order order) {
    return orderRepository.save(order);
  }

  public Page<Order> getOrdersByStatus(DeliveryStatus status, int page, int size) {
    return orderRepository.findByDeliveryStatus(status, PageRequest.of(page, size));
  }

  @Transactional
  public List<Order> lockPendingOrders(int batchSize) {
    return orderRepository.findLockedPendingOrders(batchSize);
  }

  public List<TopCustomerDTO> getTopCustomers() {
    return orderRepository.findTopDeliveredCustomers(PageRequest.of(0, 3));
  }

  public Map<DeliveryStatus, Long> getStatusCounts() {
    Map<DeliveryStatus, Long> counts = new HashMap<>();
    List<Object[]> results = orderRepository.countOrdersByStatus();
    results.forEach(result -> {
      counts.put((DeliveryStatus) result[0], (Long) result[1]);
    });
    return counts;
  }

  @Transactional
  public List<Order> lockAndProcessPendingOrders(int batchSize) {
    List<Order> orders = orderRepository.findLockedPendingOrders(batchSize);
    orders.forEach(order -> order.setDeliveryStatus(DeliveryStatus.IN_PROGRESS));
    return orderRepository.saveAll(orders);
  }

  @Transactional
  public void updateOrderStatus(Integer orderId, DeliveryStatus status) {
    orderRepository.findById(orderId).ifPresent(order -> {
      order.setDeliveryStatus(status);
      orderRepository.save(order);
    });
  }
}