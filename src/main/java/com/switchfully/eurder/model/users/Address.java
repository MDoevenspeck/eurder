package com.switchfully.eurder.model.users;

import javax.validation.constraints.NotBlank;

public class Address {
    @NotBlank
    private final String streetName;
    @NotBlank
    private final String houseNumber;
    @NotBlank
    private final String city;
    @NotBlank
    private final String postalCode;

    public Address(String streetName, String houseNumber, String city, String postalCode) {
        this.streetName = streetName;
        this.houseNumber = houseNumber;
        this.city = city;
        this.postalCode = postalCode;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }
}
