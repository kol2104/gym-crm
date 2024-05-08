package com.epam.gymcrm.dto.trainee;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TraineeForUpdateRequestDto(
        @Schema(description = "Trainee username")
        @NotEmpty String username,
        @Schema(description = "Trainee firstname")
        @NotEmpty String firstName,
        @Schema(description = "Trainee lastname")
        @NotEmpty String lastName,
        @Schema(description = "Trainee date of birth")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dateOfBirth,
        @Schema(description = "Trainee address")
        String address,
        @Schema(description = "Trainee status")
        @NotNull Boolean isActive
) {
}
