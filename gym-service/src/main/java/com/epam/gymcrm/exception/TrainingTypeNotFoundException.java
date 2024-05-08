package com.epam.gymcrm.exception;

public class TrainingTypeNotFoundException extends RuntimeException {
    public TrainingTypeNotFoundException(Long id) {
        super("Training type with id '" + id + "' not found");
    }

    public TrainingTypeNotFoundException(String name) {
        super("Training type with name '" + name + "' not found");
    }
}
