package com.epam.gymcrm.exception;

import java.time.LocalDateTime;

public class TrainingDateConstraintViolation extends RuntimeException {

    public TrainingDateConstraintViolation(LocalDateTime date) {
        super("Training date constraint violation for date: " + date);
    }
}
