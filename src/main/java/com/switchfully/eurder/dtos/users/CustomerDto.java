package com.switchfully.eurder.dtos.users;

import com.switchfully.eurder.model.users.Address;
import com.switchfully.eurder.model.users.Role;

public class CustomerDto extends UserDto{
    private final String firstname;
    private final String lastname;

    private final String email;

    private final AddressDto addressDto;

    private final String phoneNumber;

    public CustomerDto(long id, String username, Role role, String firstname, String lastname, String email, AddressDto address, String phoneNumber) {
        super(id, username, role);
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.addressDto = address;
        this.phoneNumber = phoneNumber;
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

    public AddressDto getAddress() {
        return addressDto;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
