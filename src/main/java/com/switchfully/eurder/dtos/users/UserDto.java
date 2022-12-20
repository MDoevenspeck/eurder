package com.switchfully.eurder.dtos.users;

import com.switchfully.eurder.model.users.Role;

public class UserDto {
    private final long id;
    private final String username;
    private final Role role;

    public UserDto(long id, String username, Role role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }
}

