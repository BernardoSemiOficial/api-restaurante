package com.api.restaurant.interfaces;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Payload para criação de restaurante/dono")
public record CreateRequestBodyRestaurantDTO(
        @Schema(example = "12345678000199") String cnpj,
        @Schema(example = "Italiana") String cuisineType,
        CreateUserDTO user
) {
}
