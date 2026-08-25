package com.pedro.delivery_api.dto.request;

import jakarta.validation.constraints.NotNull;

public record OrderItemRequestDTO (
    @NotNull
    Long productId,

    @NotNull
    Integer quantity
) {}
