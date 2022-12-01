package com.switchfully.eurder.controllers;

import com.switchfully.eurder.dtos.users.CreateAdminDto;
import com.switchfully.eurder.dtos.users.CreateCustomerDto;
import com.switchfully.eurder.dtos.users.UserDto;
import com.switchfully.eurder.model.users.security.Feature;
import com.switchfully.eurder.services.SecurityService;
import com.switchfully.eurder.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final SecurityService securityService;

    public UserController(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDto> getAllUsers(@RequestHeader(required = false) String authorization) {
        securityService.validateAuthorisation(authorization, Feature.GET_ALL_USERS);
        return userService.getAllUsers();
    }

    @GetMapping(params = "userType", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<? extends UserDto> getAllUsersByUserType(@RequestParam(defaultValue = "all") String userType, @RequestHeader(required = false) String authorization) {
        securityService.validateAuthorisation(authorization, Feature.GET_ALL_USERS_BY_USERTYPE);
        return userService.getAllUsersByUserType(userType);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto getCustomerById(@PathVariable String id, @RequestHeader(required = false) String authorization) {
        securityService.validateAuthorisation(authorization, Feature.GET_CUSTOMER_BY_ID);
        return userService.getCustomerById(id);
    }

    @PostMapping(params = "admin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createAdmin(@Valid @RequestBody CreateAdminDto createAdminDto, @RequestHeader(required = false) String authorization) {
        securityService.validateAuthorisation(authorization, Feature.CREATE_ADMIN);
        return userService.createAdmin(createAdminDto);
    }

    @PostMapping(params = "customer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createCustomer(@Valid @RequestBody CreateCustomerDto createCustomerDto) {
        return userService.createCustomer(createCustomerDto);
    }
}
