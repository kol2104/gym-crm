package com.epam.gymcrm.exception;

public class TrainerNotFoundException extends RuntimeException {
    public TrainerNotFoundException(Long id) {
        super("Trainer with id '" + id + "' not found");
    }

    public TrainerNotFoundException(String username) {
        super("Trainer with username '" + username + "' not found");
    }

    public TrainerNotFoundException(String username, String password) {
        super("Trainer with username '" + username + "' and password '" + password + "' not found");
    }
}
