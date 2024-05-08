package com.epam.gymcrm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TrainerWorkloadRequestDto(
    String username,
    String firstname,
    String lastname,
    Boolean isActive,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime trainingDate,
    Integer trainingDuration,
    Boolean action
) {
}
