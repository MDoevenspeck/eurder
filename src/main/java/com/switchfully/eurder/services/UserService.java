package com.switchfully.eurder.services;

import com.switchfully.eurder.dtos.users.CreateAdminDto;
import com.switchfully.eurder.dtos.users.CreateCustomerDto;
import com.switchfully.eurder.dtos.users.UserDto;
import com.switchfully.eurder.mappers.UserMapper;
import com.switchfully.eurder.model.users.Customer;
import com.switchfully.eurder.model.users.Role;
import com.switchfully.eurder.model.users.User;
import com.switchfully.eurder.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserDto> getAllUsers() {
        return userMapper.toDto(userRepository.findAll());
    }

    public List<? extends UserDto> getAllUsersByUserType(String userType) {
        if (userType.equals("customer")) {
            return userRepository.findAll().stream()
                    .filter(user -> user.getRole() == Role.CUSTOMER)
                    .map(customer -> userMapper.toDto((Customer) customer)).toList();
        }
        if (userType.equals("admin")) {
            return userRepository.findAll().stream()
                    .filter(user -> user.getRole().equals(Role.ADMIN))
                    .map(userMapper::toDto)
                    .toList();
        }
        throw new IllegalArgumentException("usertype not found");
    }

    public UserDto getCustomerById(Long id) {
        Customer user = userRepository.getCustomerByUserID(id).orElseThrow(() -> new NoSuchElementException("Customer with id " + id + " not found!"));
        return userMapper.toDto(user);
    }

    public UserDto createAdmin(CreateAdminDto createAdminDto) {
        validateUsername(createAdminDto.username());
        return userMapper.toDto(userRepository.save(userMapper.toAdmin(createAdminDto)));
    }

    public UserDto createCustomer(CreateCustomerDto createCustomerDto) {
        validateUsername(createCustomerDto.username());
        return userMapper.toDto(userRepository.save(userMapper.toCustomer(createCustomerDto)));
    }
    private void validateUsername(String username){
        if (userRepository.findByUsername(username).isPresent()){
            throw new IllegalArgumentException("Username already exist");
        }
    }
}
