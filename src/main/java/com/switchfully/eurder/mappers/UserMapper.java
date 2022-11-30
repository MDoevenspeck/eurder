package com.switchfully.eurder.mappers;

import com.switchfully.eurder.model.users.Customer;
import com.switchfully.eurder.model.users.Role;
import com.switchfully.eurder.model.users.User;
import com.switchfully.eurder.dtos.users.CreateCustomerDto;
import com.switchfully.eurder.dtos.users.CreateUserDto;
import com.switchfully.eurder.dtos.users.CustomerDto;
import com.switchfully.eurder.dtos.users.UserDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public List<UserDto> toDto(List<User> users){
        return users.stream().map(this::toDto).toList();
    }
    public UserDto toDto(User user){
        return new UserDto(user.getId(), user.getUsername(), user.getRole());
    }

    public CustomerDto toDto(Customer customer)
    {
        return new CustomerDto(customer.getId(), customer.getUsername(), customer.getRole(), customer.getFirstname(), customer.getLastname(), customer.getEmail(), customer.getAddress(), customer.getPhoneNumber());
    }

    public User toUser (CreateUserDto createUserDto){
        return new User(createUserDto.username(), createUserDto.password(),createUserDto.role());
    }
    public Customer toCustomer(CreateCustomerDto createCustomerDto){
        return new Customer(createCustomerDto.username(), createCustomerDto.password(),
                createCustomerDto.getFirstname(),createCustomerDto.getLastname(), createCustomerDto.getEmail(),
                createCustomerDto.getAddress(), createCustomerDto.getPhoneNumber());
    }
}
