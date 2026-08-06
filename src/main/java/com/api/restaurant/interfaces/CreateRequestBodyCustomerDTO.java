package com.api.restaurant.interfaces;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Payload para criação de cliente")
public record CreateRequestBodyCustomerDTO(
        @Schema(example = "12345678901") String cpf,
        CreateUserDTO user
) {
}
