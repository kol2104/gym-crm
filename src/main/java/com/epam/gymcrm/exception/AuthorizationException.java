package com.epam.gymcrm.exception;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException() {
        super("User has not enough rights");
    }
}
