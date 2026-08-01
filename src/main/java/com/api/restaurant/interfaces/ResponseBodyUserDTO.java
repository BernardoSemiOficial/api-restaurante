package com.api.restaurant.interfaces;

import com.api.restaurant.model.User;

public record ResponseBodyUserDTO(
        Long id,
        String name,
        String email,
        String login,
        ResponseBodyAddressDTO address
) {
    public ResponseBodyUserDTO(User user) {
        this(user.getId(), user.getName(), user.getEmail(), user.getLogin(), new ResponseBodyAddressDTO(user.getAddress()));
    }
}
