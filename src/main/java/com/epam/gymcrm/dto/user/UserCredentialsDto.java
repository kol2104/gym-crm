package com.epam.gymcrm.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserCredentialsDto(
        @Schema(description = "User username")
        @NotEmpty String username,
        @Schema(description = "User password")
        @NotEmpty @Size(min = 3) String password
) {
}
