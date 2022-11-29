package com.switchfully.eurder.exceptions;

public class UnknownUserException extends RuntimeException{
    public UnknownUserException() {
        super("Unauthorized");
    }
}