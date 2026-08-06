package com.api.restaurant.interfaces;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Payload para troca de senha do dono")
public record EditRequestBodyRestaurantPasswordDTO(
        @Schema(example = "Senha@123") String currentPassword,
        @Schema(example = "NovaSenha@456") String newPassword
) {
}
