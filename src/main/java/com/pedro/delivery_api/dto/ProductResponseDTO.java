package com.pedro.delivery_api.dto;

import java.math.BigDecimal;

public record ProductResponseDTO (
        Long id,
        String name,
        String description,
        BigDecimal price,
        Boolean available,
        CategoryResponseDTO category
) {}

