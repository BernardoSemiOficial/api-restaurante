package com.api.restaurant.interfaces;

import com.api.restaurant.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados públicos do usuário")
public record ResponseBodyUserDTO(
        @Schema(example = "1") Long id,
        @Schema(example = "João Silva") String name,
        @Schema(example = "joao@email.com") String email,
        @Schema(example = "joao.silva") String login,
        ResponseBodyAddressDTO address
) {
    public ResponseBodyUserDTO(User user) {
        this(user.getId(), user.getName(), user.getEmail(), user.getLogin(), new ResponseBodyAddressDTO(user.getAddress()));
    }
}
