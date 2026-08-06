package com.api.restaurant.interfaces;

public record EditRequestBodyRestaurantPasswordDTO(String currentPassword, String newPassword) {
}
