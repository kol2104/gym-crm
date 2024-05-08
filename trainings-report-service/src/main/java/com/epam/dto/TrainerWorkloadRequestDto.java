package com.epam.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TrainerWorkloadRequestDto(
    @Schema(description = "Trainer username")
    @NotEmpty String username,
    @Schema(description = "Trainer firstname")
    @NotEmpty String firstname,
    @Schema(description = "Trainer lastname")
    @NotEmpty String lastname,
    @Schema(description = "Trainer status")
    @NotNull Boolean isActive,
    @Schema(description = "Training date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull LocalDateTime trainingDate,
    @Schema(description = "Training duration")
    @NotNull Long trainingDuration,
    @Schema(description = "Action (add or delete trainings from workload)")
    @NotNull Boolean action
) {
}
