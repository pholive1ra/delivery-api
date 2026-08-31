package com.pedro.delivery_api.dto.response;

import com.pedro.delivery_api.entity.Role;

public record RegisterUserResponseDTO(String email, String name, Role role) {
}
