package com.epam.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

public record YearDto(
    @Schema(description = "Year")
    Integer yearNumber,
    @Schema(description = "Summary workload by months")
    Map<String, Long> summaryDurationByMonths
) {
}
