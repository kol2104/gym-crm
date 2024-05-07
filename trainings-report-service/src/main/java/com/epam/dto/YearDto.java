package com.epam.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record YearDto(
    @Schema(description = "Year number")
    Integer yearNumber,
    @Schema(description = "Months")
    List<MonthDto> months
) {
}
