package com.api.restaurant.interfaces;

import com.api.restaurant.model.Address;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Endereço na resposta")
public record ResponseBodyAddressDTO(
        @Schema(example = "01310-100") String zipCode,
        @Schema(example = "Av. Paulista") String street,
        @Schema(example = "1000") String number,
        @Schema(example = "São Paulo") String city,
        @Schema(example = "SP") String state
) {
    public ResponseBodyAddressDTO(Address address) {
        this(address.getZipCode(), address.getStreet(), address.getNumber(), address.getCity(), address.getState());
    }
}
