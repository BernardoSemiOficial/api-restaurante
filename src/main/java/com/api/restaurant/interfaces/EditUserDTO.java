package com.api.restaurant.interfaces;

public record EditUserDTO(String name, String email, String login, String password, CreateAddressDTO address) {}
