package com.api.restaurant.interfaces;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Payload para atualização de restaurante (sem senha)")
public record EditRequestBodyRestaurantDTO(
        @Schema(example = "12345678000199") String cnpj,
        @Schema(example = "Japonesa") String cuisineType,
        EditUserDTO user
) {
}
