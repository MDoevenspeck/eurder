package com.switchfully.eurder.dtos.users;

import javax.validation.constraints.NotBlank;

public record AddressDto(@NotBlank String streetName,@NotBlank String houseNumber,@NotBlank String city,@NotBlank String postalCode) {

}
