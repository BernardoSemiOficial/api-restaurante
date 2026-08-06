package com.api.restaurant.interfaces;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados do usuário na edição (sem senha)")
public record EditUserDTO(
        @Schema(example = "João Silva") String name,
        @Schema(example = "joao@email.com") String email,
        @Schema(example = "joao.silva") String login,
        CreateAddressDTO address
) {}
