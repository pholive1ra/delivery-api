package com.pedro.delivery_api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDTO (
        @NotBlank String name
) {}
