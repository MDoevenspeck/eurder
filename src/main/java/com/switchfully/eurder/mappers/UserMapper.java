package com.switchfully.eurder.mappers;

import com.switchfully.eurder.dtos.users.*;
import com.switchfully.eurder.model.users.Address;
import com.switchfully.eurder.model.users.Customer;
import com.switchfully.eurder.model.users.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public List<UserDto> toDto(List<User> users) {
        return users.stream().map(this::toDto).toList();
    }

    public UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getRole());
    }

    public CustomerDto toDto(Customer customer) {
        return new CustomerDto(
                customer.getId(),
                customer.getUsername(),
                customer.getRole(),
                customer.getFirstname(),
                customer.getLastname(),
                customer.getEmail(),
                new AddressDto(
                        customer.getAddress().streetName(),
                        customer.getAddress().houseNumber(),
                        customer.getAddress().city(),
                        customer.getAddress().postalCode()),
                customer.getPhoneNumber());
    }

    public User toAdmin(CreateAdminDto createAdminDto) {
        return new User(createAdminDto.username(), createAdminDto.password(), createAdminDto.role());
    }

    public Customer toCustomer(CreateCustomerDto createCustomerDto) {
        return new Customer(createCustomerDto.username(),
                createCustomerDto.password(),
                createCustomerDto.getFirstname(),
                createCustomerDto.getLastname(),
                createCustomerDto.getEmail(),
                new Address(createCustomerDto.getAddressDto().streetName(),
                        createCustomerDto.getAddressDto().houseNumber(),
                        createCustomerDto.getAddressDto().city(),
                        createCustomerDto.getAddressDto().postalCode()),
                createCustomerDto.getPhoneNumber());
    }
}
