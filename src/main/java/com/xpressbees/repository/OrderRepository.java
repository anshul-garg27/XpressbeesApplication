package com.xpressbees.repository;

import com.xpressbees.DTO.TopCustomerDTO;
import com.xpressbees.enums.DeliveryStatus;
import com.xpressbees.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Integer> {
  Page<Order> findByDeliveryStatus(DeliveryStatus status, Pageable pageable);
  @Query(value = """
    SELECT * FROM orders 
    WHERE delivery_status = 'PENDING' 
    ORDER BY created_at ASC 
    LIMIT :limit 
    FOR UPDATE""",
    nativeQuery = true)
  List<Order> findLockedPendingOrders(@Param("limit") int limit);

  @Query("SELECT NEW com.xpressbees.DTO.TopCustomerDTO(o.customerName, COUNT(o)) " +
      "FROM Order o WHERE o.deliveryStatus = 'DELIVERED' " +
      "GROUP BY o.customerName ORDER BY COUNT(o) DESC, o.customerName ASC")
  List<TopCustomerDTO> findTopDeliveredCustomers(Pageable pageable);

  @Query("SELECT o.deliveryStatus, COUNT(o) FROM Order o GROUP BY o.deliveryStatus")
  List<Object[]> countOrdersByStatus();

  // Add to repository interface:
  @Query("SELECT COUNT(o) FROM Order o WHERE o.deliveryStatus = 'PENDING'")
  long countByDeliveryStatusPending();

  @Query("SELECT COUNT(o) FROM Order o WHERE o.deliveryStatus = 'IN_PROGRESS'")
  long countByDeliveryStatusInProgress();

  @Query("SELECT COUNT(o) FROM Order o WHERE o.deliveryStatus = 'DELIVERED'")
  long countByDeliveryStatusDelivered();


}