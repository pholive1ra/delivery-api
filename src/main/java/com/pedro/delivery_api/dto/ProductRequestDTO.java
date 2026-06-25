package com.pedro.delivery_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequestDTO (
        @NotNull
        Long categoryId,
        @NotBlank
        Boolean available,
        @NotBlank
        String name,
        String description,
        @NotNull
        @Positive
        BigDecimal price
) {}
