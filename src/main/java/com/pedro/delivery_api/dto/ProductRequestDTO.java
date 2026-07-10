package com.pedro.delivery_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ProductRequestDTO (
        @NotNull
        Long categoryId,
        @NotNull
        Boolean available,
        @NotNull
        @PositiveOrZero
        Integer stockQuantity,
        @NotBlank
        String name,
        String description,
        @NotNull
        @Positive
        BigDecimal price
) {}
