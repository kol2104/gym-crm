package com.epam.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record MonthDto(
    @Schema(description = "Moth name")
    String name,
    @Schema(description = "Summary duration of trainings for this month")
    Long summaryDuration
) {
}
