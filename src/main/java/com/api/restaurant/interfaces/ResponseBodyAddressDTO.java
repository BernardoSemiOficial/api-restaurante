package com.api.restaurant.interfaces;

import com.api.restaurant.model.Address;

public record ResponseBodyAddressDTO(
        String zipCode,
        String street,
        String number,
        String city,
        String state
) {
    public ResponseBodyAddressDTO(Address address) {
        this(address.getZipCode(), address.getStreet(), address.getNumber(), address.getCity(), address.getState());
    }
}
