package com.epam.gymcrm.controller;

import com.epam.gymcrm.auth.annotation.Authenticated;
import com.epam.gymcrm.dto.user.UserNewPasswordRequestDto;
import com.epam.gymcrm.exception.model.ExceptionResponse;
import com.epam.gymcrm.model.Role;
import com.epam.gymcrm.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User controller", description = "Common user operations for trainee and trainers")
public class UserController {

    private final UserService userService;

    @ApiResponse(responseCode = "200", description = "Password changed successfully")
    @ApiResponse(responseCode = "400", description = "Provided data to change password is not valid",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "401", description = "User is unauthorized",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "403", description = "User has not enough rights",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "404", description = "User with specified username and password not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @Operation(summary = "Change password", description = "Endpoint to change password")
    @PutMapping("/password")
    @Authenticated(roles = {Role.TRAINEE, Role.TRAINER})
    public void changePassword(@RequestBody @Valid UserNewPasswordRequestDto userNewPasswordRequestDto,
                               @RequestHeader(name = "Authorization", required = false) String token) {
        log.info("Start process of changing password for user");
        userService.updatePassword(userNewPasswordRequestDto, token);
    }
}
