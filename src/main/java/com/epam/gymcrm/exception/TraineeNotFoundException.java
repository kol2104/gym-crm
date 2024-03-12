package com.epam.gymcrm.exception;

public class TraineeNotFoundException extends RuntimeException {
    public TraineeNotFoundException(Long id) {
        super("Trainee with id '" + id + "' not found");
    }
}
