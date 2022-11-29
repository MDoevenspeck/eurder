package com.switchfully.eurder.repositories;

import com.switchfully.eurder.exceptions.UserAlreadyExistsException;
import com.switchfully.eurder.model.users.Role;
import com.switchfully.eurder.model.users.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepository {
    private final Map<String, User> users = new HashMap<>();

    public UserRepository() {
        User admin= new User("admin","root", Role.ADMIN);
        users.put(admin.getId(),admin);
    }

    public Optional<User> getUserByUsername(String username){
        return users.values().stream().filter(user -> user.getUsername().equals(username)).findFirst();
    }

    public Optional<User> getUserById(String id) {
        return Optional.ofNullable(users.get(id));
    }

    public List<User> getAllUsers(){
        return users.values().stream().toList();
    }

    public User saveUser(User user){
        users.values().stream().filter(userInRepository -> userInRepository.getUsername().equals(user.getUsername())).findAny().ifPresent(user1 -> {
            throw new UserAlreadyExistsException();
        });
        users.put(user.getId(), user);
        return user;
    }

}

