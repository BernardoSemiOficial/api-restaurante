package com.api.restaurant.interfaces;

import com.api.restaurant.model.Customer;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de cliente")
public record ResponseBodyCustomerDTO(
        @Schema(example = "1") Long id,
        @Schema(example = "12345678901") String cpf,
        ResponseBodyUserDTO user
) {
    public ResponseBodyCustomerDTO(Customer customer) {
        this(customer.getId(), customer.getCpf(), new ResponseBodyUserDTO(customer.getUser()));
    }
}
