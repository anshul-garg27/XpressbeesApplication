package com.xpressbees.DTO;

import com.xpressbees.enums.DeliveryStatus;
import java.time.LocalDateTime;

public record OrderResponseDTO(
    Integer orderId,
    String customerName,
    String address,
    DeliveryStatus deliveryStatus,
    LocalDateTime createdAt) {}