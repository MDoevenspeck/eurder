package com.switchfully.eurder.dtos.users;

import com.switchfully.eurder.model.users.Role;

public class UserDto {
    private final String id;
    private final String username;
    private final Role role;

    public UserDto(String id, String username, Role role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }
}

