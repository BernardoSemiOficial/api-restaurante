package com.api.restaurant.interfaces;

public record EditRequestBodyCustomerPasswordDTO(String currentPassword, String newPassword) {
}
