package com.epam.gymcrm.dto.trainee;

import com.epam.gymcrm.dto.trainer.PlainTrainerResponseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record TraineeResponseDto(
        @Schema(description = "Trainee firstname")
        String firstName,
        @Schema(description = "Trainee lastname")
        String lastName,
        @Schema(description = "Trainee username")
        String username,
        @Schema(description = "Trainee date of birth")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dateOfBirth,
        @Schema(description = "Trainee address")
        String address,
        @Schema(description = "Trainee status")
        Boolean isActive,
        @Schema(description = "Trainee trainer list")
        List<PlainTrainerResponseDto> trainersList
) {
}
