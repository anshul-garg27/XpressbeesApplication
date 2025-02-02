package com.xpressbees.controller;

import com.xpressbees.DTO.OrderDTO;
import com.xpressbees.DTO.OrderResponseDTO;
import com.xpressbees.DTO.TopCustomerDTO;
import com.xpressbees.enums.DeliveryStatus;
import com.xpressbees.model.Order;
import com.xpressbees.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping("/orders")
  public ResponseEntity<OrderResponseDTO> createOrder(
      @Valid @RequestBody OrderDTO orderDTO) {

    Order order = new Order();
    order.setCustomerName(orderDTO.customerName());
    order.setAddress(orderDTO.address());

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(convertToResponseDTO(orderService.createOrder(order)));
  }



  @GetMapping("/orders")
  public ResponseEntity<Page<Order>> getOrdersByStatus(
      @RequestParam(required = true) DeliveryStatus status,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    return ResponseEntity.ok(orderService.getOrdersByStatus(status, page, size));
  }

  @GetMapping("/orders/status-count")
  public ResponseEntity<Map<DeliveryStatus, Long>> getStatusCounts() {
    return ResponseEntity.ok(orderService.getStatusCounts());
  }


  @GetMapping("/customers/top")
  public ResponseEntity<List<TopCustomerDTO>> getTopCustomers() {
    return ResponseEntity.ok(orderService.getTopCustomers());
  }

  private OrderResponseDTO convertToResponseDTO(Order order) {
    return new OrderResponseDTO(
        order.getOrderId(),
        order.getCustomerName(),
        order.getAddress(),
        order.getDeliveryStatus(),
        order.getCreatedAt()
    );
  }
}