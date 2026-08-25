package com.pedro.delivery_api.dto.response;

public record CustomerResponseDTO (
        Long id,
        String name,
        String email,
        String phone
) {}
