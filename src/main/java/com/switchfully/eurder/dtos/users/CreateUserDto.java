package com.switchfully.eurder.dtos.users;

import com.switchfully.eurder.model.users.Role;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateUserDto {
    @NotBlank
    private final String username;
    @NotBlank
    private final String password;
    @NotNull
    private Role role;

    public CreateUserDto(String username, String password) {
        this.username = username;
        this.password = password;

    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }

    public Role role() {
        return role;
    }
    public void setRole(Role role){
        this.role = role;
    }
}
