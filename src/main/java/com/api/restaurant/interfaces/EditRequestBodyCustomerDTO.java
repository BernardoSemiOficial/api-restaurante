package com.api.restaurant.interfaces;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Payload para atualização de cliente (sem senha)")
public record EditRequestBodyCustomerDTO(
        @Schema(example = "12345678901") String cpf,
        EditUserDTO user
) {
}
