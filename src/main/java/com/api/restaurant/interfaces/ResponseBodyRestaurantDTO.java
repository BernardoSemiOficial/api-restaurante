package com.api.restaurant.interfaces;

import com.api.restaurant.model.Restaurant;

public record ResponseBodyRestaurantDTO(Long id, String cnpj, String cuisineType, ResponseBodyUserDTO user) {
    public ResponseBodyRestaurantDTO (Restaurant restaurant) {
        this(restaurant.getId(), restaurant.getCnpj(), restaurant.getCuisineType(), new ResponseBodyUserDTO(restaurant.getUser()));
    }
}
