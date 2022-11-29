package com.switchfully.eurder.exceptions;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException() {
        super("Unauthorized");
    }
}
