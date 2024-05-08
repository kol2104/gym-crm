package com.epam.gymcrm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record TokenDto(
        @Schema(description = "Generated token for application")
        String token
) {
}
