package com.switchfully.eurder.services;

import com.switchfully.eurder.dtos.users.CreateAdminDto;
import com.switchfully.eurder.dtos.users.CreateCustomerDto;
import com.switchfully.eurder.dtos.users.CustomerDto;
import com.switchfully.eurder.dtos.users.UserDto;
import com.switchfully.eurder.mappers.UserMapper;
import com.switchfully.eurder.model.users.Customer;
import com.switchfully.eurder.model.users.User;
import com.switchfully.eurder.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserDto> getAllUsers() {
        return userMapper.toDto(userRepository.getAllUsers());
    }

    public List<CustomerDto> getAllCustomers() {
        return userRepository.getAllUsers().stream().filter(user -> user instanceof Customer).map(customer -> userMapper.toDto((Customer) customer)).toList();
    }

    public UserDto getCustomerById(String id) {
        User user = userRepository.getUserById(id).orElseThrow(() -> new NoSuchElementException("User with id " + id + " not found!"));
        if (user instanceof Customer) {
            return userMapper.toDto((Customer) user);
        }
        return userMapper.toDto(user);
    }

    public UserDto createAdmin(CreateAdminDto createAdminDto) {
        return userMapper.toDto(userRepository.saveUser(userMapper.toUser(createAdminDto)));
    }

    public UserDto createCustomer(CreateCustomerDto createCustomerDto) {
        return userMapper.toDto(userRepository.saveUser(userMapper.toCustomer(createCustomerDto)));
    }
}
