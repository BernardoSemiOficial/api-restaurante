package com.api.restaurant.interfaces;

public record CreateRequestBodyRestaurantDTO(String cnpj, String cuisineType, CreateUserDTO user) {
}
