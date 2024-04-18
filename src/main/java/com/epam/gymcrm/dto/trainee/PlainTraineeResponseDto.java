package com.epam.gymcrm.dto.trainee;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record PlainTraineeResponseDto(
        @Schema(description = "Trainee firstname")
        String firstName,
        @Schema(description = "Trainee lastname")
        String lastName,
        @Schema(description = "Trainee username")
        String username
) {
}
