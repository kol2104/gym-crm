package com.epam.gymcrm.dto.training;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record TrainingDto(
        @Schema(description = "Training name")
        @NotEmpty String trainingName,
        @Schema(description = "Training date")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @NotNull LocalDateTime trainingDate,
        @Schema(description = "Training type name")
        @NotEmpty String trainingTypeName,
        @Schema(description = "Training duration")
        @NotNull @Positive Long trainingDuration,
        @Schema(description = "Trainee username for training")
        @NotEmpty String traineeUsername,
        @Schema(description = "Trainer username for training")
        @NotEmpty String trainerUsername
) {
}
