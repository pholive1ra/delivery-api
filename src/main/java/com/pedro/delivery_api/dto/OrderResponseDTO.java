package com.pedro.delivery_api.dto;

import com.pedro.delivery_api.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO (
        Long id,
        Long customerId,
        Long addressId,
        List<OrderItemResponseDTO> items,
        BigDecimal totalPrice,
        OrderStatus orderStatus,
        LocalDateTime createdAt
) {}
