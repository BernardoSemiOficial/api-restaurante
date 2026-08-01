package com.api.restaurant.interfaces;

public record CreateUserDTO (String name, String email, String login, String password, CreateAddressDTO address) {}
