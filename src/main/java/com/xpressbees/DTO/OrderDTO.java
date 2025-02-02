package com.xpressbees.DTO;

import jakarta.validation.constraints.NotBlank;

public record OrderDTO(
    @NotBlank(message = "Customer name is required")
    String customerName,

    @NotBlank(message = "Delivery address is required")
    String address
) {}