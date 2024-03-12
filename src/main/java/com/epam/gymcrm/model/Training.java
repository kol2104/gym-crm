package com.epam.gymcrm.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Training {
    private Long id;
    private Long traineeId;
    private Long trainerId;
    private String trainingName;
    private TrainingType trainingType;
    private LocalDateTime trainingDate;
    private int trainingDuration;
}
