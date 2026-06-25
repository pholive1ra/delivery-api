package com.pedro.delivery_api.dto;

public record AddressResponseDTO (
        Long id,
        String street,
        String number,
        String complement,
        String neighborhood,
        String city,
        String zipCode,
        Long customerId
) {}

