package com.api.restaurant.interfaces;

public record ResponseBodyRestaurantDTO(Long id, String cnpj, String cuisineType, ResponseBodyUserDTO user) {
}
