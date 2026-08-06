package com.api.restaurant.interfaces;

import com.api.restaurant.model.Restaurant;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de restaurante")
public record ResponseBodyRestaurantDTO(
        @Schema(example = "1") Long id,
        @Schema(example = "12345678000199") String cnpj,
        @Schema(example = "Italiana") String cuisineType,
        ResponseBodyUserDTO user
) {
    public ResponseBodyRestaurantDTO(Restaurant restaurant) {
        this(restaurant.getId(), restaurant.getCnpj(), restaurant.getCuisineType(), new ResponseBodyUserDTO(restaurant.getUser()));
    }
}
