package com.epam.gymcrm.exception;

public class TraineeNotFoundException extends RuntimeException {
    public TraineeNotFoundException() {
        super("Trainee not found");
    }

    public TraineeNotFoundException(Long id) {
        super("Trainee with id '" + id + "' not found");
    }

    public TraineeNotFoundException(String username) {
        super("Trainee with username '" + username + "' not found");
    }
}
