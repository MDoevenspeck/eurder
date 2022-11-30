package com.switchfully.eurder.model.users;


public class Customer extends User{

    private final String firstname;
    private final String lastname;
    private final String email;
    private final Address address;
    private final String phoneNumber;

    public Customer(String username, String password, String firstname, String lastname, String email, Address address, String phoneNumber) {
        super(username, password, Role.CUSTOMER);
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.address = address;
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

    public Address getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
