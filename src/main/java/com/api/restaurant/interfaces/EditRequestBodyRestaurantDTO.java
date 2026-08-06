package com.api.restaurant.interfaces;

public record EditRequestBodyRestaurantDTO(String cnpj, String cuisineType, EditUserDTO user) {
}
