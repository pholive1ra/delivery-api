package com.pedro.delivery_api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CustomerRequestDTO (
        @NotBlank
        String name,
        @NotBlank
        String email,
        @NotBlank
        String phone
) {}
