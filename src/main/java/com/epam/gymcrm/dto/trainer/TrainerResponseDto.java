package com.epam.gymcrm.dto.trainer;

import com.epam.gymcrm.dto.TrainingTypeDto;
import com.epam.gymcrm.dto.trainee.PlainTraineeResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record TrainerResponseDto(
        @Schema(description = "Trainer firstname")
        String firstName,
        @Schema(description = "Trainer lastname")
        String lastName,
        @Schema(description = "Trainer username")
        String username,
        @Schema(description = "Trainer specialization (training type)")
        TrainingTypeDto specialization,
        @Schema(description = "Trainer status")
        Boolean isActive,
        @Schema(description = "Trainer trainee list")
        List<PlainTraineeResponseDto> traineesList
) {
}
