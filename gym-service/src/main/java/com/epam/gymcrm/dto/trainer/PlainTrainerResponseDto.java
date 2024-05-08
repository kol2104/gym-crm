package com.epam.gymcrm.dto.trainer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record PlainTrainerResponseDto(
        @Schema(description = "Trainer firstname")
        String firstName,
        @Schema(description = "Trainer lastname")
        String lastName,
        @Schema(description = "Trainer username")
        String username,
        @Schema(description = "Trainer specialization (training type id)")
        Long specializationId
) {
}
