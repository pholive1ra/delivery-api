package com.pedro.delivery_api.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record RegisterUserRequestDTO(@NotEmpty(message = "Nome obrigatório") String name,
                                     @NotEmpty(message = "E-mail obrigatório") String email,
                                     @NotEmpty(message = "Senha obrigatória") String password) {
}
