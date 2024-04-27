package com.epam.gymcrm.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserNewPasswordRequestDto(
        @Schema(description = "Old user password to change")
        @NotEmpty String oldPassword,
        @Schema(description = "New user password change to")
        @NotEmpty @Size(min = 3) String newPassword
) {
}
