package com.epam.gymcrm.dto.trainer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

public record TrainerUsernameDto(
        @Schema(description = "Trainer username")
        @NotEmpty String username
) {
}
