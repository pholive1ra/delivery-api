package com.pedro.delivery_api.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderRequestDTO (
        @NotNull
        Long customerId,
        @NotNull
        Long addressId,
        @NotNull
        List<OrderItemRequestDTO> items
) {}

