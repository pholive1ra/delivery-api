package com.pedro.delivery_api.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDTO (
        @NotBlank String name
) {}
