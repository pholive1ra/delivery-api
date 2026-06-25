package com.pedro.delivery_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerRequestDTO (
        @NotBlank
        String name,
        @NotBlank
        String email,
        @NotBlank
        String phone
) {}
