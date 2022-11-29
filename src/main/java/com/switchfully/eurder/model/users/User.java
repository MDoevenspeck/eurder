package com.switchfully.eurder.model.users;

import java.util.UUID;


public class User {

    private final String id;
    private final String username;
    private final String password;
    private final Role role;

    public User(String username, String password, Role role) {
        this.id = String.valueOf(UUID.randomUUID());
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public boolean doesPasswordMatch(String password) {
        return this.password.equals(password);
    }

    public boolean hasAccessTo(Feature feature) {
        return this.role.hasFeature(feature);
    }
}
