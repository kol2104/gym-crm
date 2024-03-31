package com.epam.gymcrm.exception;

public class TraineeNotFoundException extends RuntimeException {
    public TraineeNotFoundException(Long id) {
        super("Trainee with id '" + id + "' not found");
    }

    public TraineeNotFoundException(String username) {
        super("Trainee with username '" + username + "' not found");
    }

    public TraineeNotFoundException(String username, String password) {
        super("Trainee with username '" + username + "' and password '" + password + "' not found");
    }
}
