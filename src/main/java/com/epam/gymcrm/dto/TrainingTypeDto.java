package com.epam.gymcrm.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record TrainingTypeDto(
        @Schema(description = "Training type id")
        Long id,
        @Schema(description = "Training type name")
        String name
) {
}
