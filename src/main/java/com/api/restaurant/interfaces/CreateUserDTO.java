package com.api.restaurant.interfaces;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados do usuário na criação")
public record CreateUserDTO(
        @Schema(example = "João Silva") String name,
        @Schema(example = "joao@email.com") String email,
        @Schema(example = "joao.silva") String login,
        @Schema(example = "Senha@123") String password,
        CreateAddressDTO address
) {}
