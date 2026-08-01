package com.api.restaurant.interfaces;

import com.api.restaurant.model.Customer;

public record ResponseBodyCustomerDTO(
        Long id,
        String cpf,
        ResponseBodyUserDTO user
) {
    public ResponseBodyCustomerDTO(Customer customer) {
        this(customer.getId(), customer.getCpf(), new ResponseBodyUserDTO(customer.getUser()));
    }
}

