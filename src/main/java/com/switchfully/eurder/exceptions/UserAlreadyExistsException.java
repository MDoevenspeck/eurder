package com.switchfully.eurder.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super("User Already exists!");
    }
}
