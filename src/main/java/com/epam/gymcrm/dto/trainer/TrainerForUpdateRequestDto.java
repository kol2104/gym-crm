package com.epam.gymcrm.dto.trainer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TrainerForUpdateRequestDto(
        @Schema(description = "Trainer username")
        @NotEmpty String username,
        @Schema(description = "Trainer firstname")
        @NotEmpty String firstName,
        @Schema(description = "Trainer lastname")
        @NotEmpty String lastName,
        @Schema(description = "Trainer specialization (training type id)")
        @NotNull @Positive Long specializationId,
        @Schema(description = "Trainer status")
        @NotNull Boolean isActive
) {
}
