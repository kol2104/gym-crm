package com.epam.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record TrainerWorkloadResponseDto(
    @Schema(description = "Trainer username")
    String username,
    @Schema(description = "Trainer firstname")
    String firstname,
    @Schema(description = "Trainer lastname")
    String lastname,
    @Schema(description = "Trainer status")
    Boolean isActive,
    @Schema(description = "Trainer's summary workload by years")
    List<YearDto> years
) {
}
