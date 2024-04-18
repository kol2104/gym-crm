package com.epam.gymcrm.controller;

import com.epam.gymcrm.aspect.annotation.TraceRequest;
import com.epam.gymcrm.dto.TokenDto;
import com.epam.gymcrm.dto.user.UserCredentialsDto;
import com.epam.gymcrm.exception.model.ExceptionResponse;
import com.epam.gymcrm.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication controller", description = "Controller for user authentication operations")
public class AuthenticationController {

    private final UserService userService;

    @ApiResponse(responseCode = "200", description = "User login successfully, return generated token")
    @ApiResponse(responseCode = "400", description = "Provided credentials is not valid",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "404", description = "User with specified username and password not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @Operation(summary = "User login", description = "Endpoint to login user using username and password")
    @GetMapping("/login")
    @TraceRequest
    public TokenDto login(HttpServletResponse response, @RequestBody @Valid UserCredentialsDto userCredentialsDto) {
        log.info("Start process of login with username '{}' and password", userCredentialsDto.username());
        TokenDto tokenDto = userService.authenticateUser(userCredentialsDto);
        response.addHeader("Authorization", tokenDto.token());
        return tokenDto;
    }
}
