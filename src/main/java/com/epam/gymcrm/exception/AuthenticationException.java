package com.epam.gymcrm.exception;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {
        super("User is not authenticated");
    }
}
