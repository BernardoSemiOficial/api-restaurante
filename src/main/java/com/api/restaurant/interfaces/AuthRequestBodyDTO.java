package com.api.restaurant.interfaces;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Credenciais de login")
public record AuthRequestBodyDTO(
        @Schema(description = "Login do usuário", example = "joao.silva")
        String login,
        @Schema(description = "Senha do usuário", example = "Senha@123")
        String password
) {
}
