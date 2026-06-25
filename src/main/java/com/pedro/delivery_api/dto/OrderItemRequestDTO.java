package com.pedro.delivery_api.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrderItemRequestDTO (
    @NotNull
    Long productId,

    @NotNull
    Integer quantity
) {}
