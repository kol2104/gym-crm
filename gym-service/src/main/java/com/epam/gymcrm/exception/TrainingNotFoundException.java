package com.epam.gymcrm.exception;

public class TrainingNotFoundException extends RuntimeException {
    public TrainingNotFoundException(Long id) {
        super("Training with id '" + id + "' not found");
    }
}
