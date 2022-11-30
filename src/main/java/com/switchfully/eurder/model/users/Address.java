package com.switchfully.eurder.model.users;

import javax.validation.constraints.NotBlank;

public record Address(@NotBlank String streetName, @NotBlank String houseNumber, @NotBlank String city,
                      @NotBlank String postalCode) {
    public Address(String streetName, String houseNumber, String city, String postalCode) {
        this.streetName = streetName;
        this.houseNumber = houseNumber;
        this.city = city;
        this.postalCode = postalCode;
    }

    @Override
    public String streetName() {
        return streetName;
    }

    @Override
    public String houseNumber() {
        return houseNumber;
    }

    @Override
    public String city() {
        return city;
    }

    @Override
    public String postalCode() {
        return postalCode;
    }
}
