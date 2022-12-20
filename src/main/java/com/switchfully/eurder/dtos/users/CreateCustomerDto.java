package com.switchfully.eurder.dtos.users;

import com.switchfully.eurder.model.users.Address;
import com.switchfully.eurder.model.users.Role;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateCustomerDto extends CreateUserDto {
    @NotBlank
    private final String firstname;
    @NotBlank
    private final String lastname;
    @NotBlank
    @Email
    private final String email;
    @NotNull
    @Valid
    private final AddressDto addressDto;
    @NotBlank
    private final String phoneNumber;

    public CreateCustomerDto(String username, String password, String firstname, String lastname, String email, AddressDto address, String phoneNumber) {
        super(username, password);
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.addressDto = address;
        this.phoneNumber = phoneNumber;
        super.setRole(Role.CUSTOMER);
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public AddressDto getAddressDto() {
        return addressDto;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
