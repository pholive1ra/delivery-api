package com.pedro.delivery_api.dto.response;

import java.math.BigDecimal;

public record OrderItemResponseDTO (
        Long productId,
        String productName,
        Integer quantity,
        BigDecimal unitPrice
) {}
