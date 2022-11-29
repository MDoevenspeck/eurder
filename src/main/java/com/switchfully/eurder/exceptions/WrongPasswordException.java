package com.switchfully.eurder.exceptions;

public class WrongPasswordException extends RuntimeException{
    public WrongPasswordException() {
        super("Unauthorized");
    }
}