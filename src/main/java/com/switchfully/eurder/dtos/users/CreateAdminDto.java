package com.switchfully.eurder.dtos.users;

import com.switchfully.eurder.model.users.Role;

public class CreateAdminDto extends CreateUserDto {
    public CreateAdminDto(String username, String password) {
        super(username, password);
        super.setRole(Role.ADMIN);
    }
}
