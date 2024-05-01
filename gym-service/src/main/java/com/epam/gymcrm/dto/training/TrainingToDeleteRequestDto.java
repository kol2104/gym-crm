package com.epam.gymcrm.dto.training;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TrainingToDeleteRequestDto(
    @Schema(description = "Training date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull LocalDateTime trainingDate,
    @Schema(description = "Trainee username for training")
    @NotEmpty String traineeUsername,
    @Schema(description = "Trainer username for training")
    @NotEmpty String trainerUsername
) {
}
