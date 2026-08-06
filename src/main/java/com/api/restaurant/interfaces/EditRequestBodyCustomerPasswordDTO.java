package com.api.restaurant.interfaces;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Payload para troca de senha do cliente")
public record EditRequestBodyCustomerPasswordDTO(
        @Schema(example = "Senha@123") String currentPassword,
        @Schema(example = "NovaSenha@456") String newPassword
) {
}
