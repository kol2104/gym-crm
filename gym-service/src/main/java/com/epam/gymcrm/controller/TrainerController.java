package com.epam.gymcrm.controller;

import com.epam.gymcrm.aspect.annotation.TraceRequest;
import com.epam.gymcrm.dto.user.UserCredentialsDto;
import com.epam.gymcrm.dto.trainer.TrainerForUpdateRequestDto;
import com.epam.gymcrm.dto.trainer.TrainerRequestDto;
import com.epam.gymcrm.dto.trainer.TrainerResponseDto;
import com.epam.gymcrm.exception.model.ExceptionResponse;
import com.epam.gymcrm.service.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trainers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Trainer controller", description = "Trainer user operations")
public class TrainerController {

    private final TrainerService trainerService;

    @ApiResponse(responseCode = "200", description = "Return trainer profile")
    @ApiResponse(responseCode = "401", description = "User is unauthorized",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "403", description = "User has not enough rights",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @Operation(summary = "Get trainer profile", description = "Get trainer profile with specified username")
    @GetMapping("/{username}")
    @TraceRequest
    public TrainerResponseDto getTrainerProfile(@PathVariable("username") String username) {
        log.info("Start process of getting trainer with username '{}'", username);
        return trainerService.getByUsername(username);
    }

    @ApiResponse(responseCode = "201", description = "Return credentials of created new trainer user")
    @ApiResponse(responseCode = "400", description = "Provided trainer to save is not valid",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @Operation(summary = "Create new trainee", description = "Create new trainee and return credentials of new trainee")
    @PostMapping
    @TraceRequest
    public ResponseEntity<UserCredentialsDto> createTrainer(@RequestBody @Valid TrainerRequestDto trainerRequestDto) {
        log.info("Start process of saving new trainer");
        return new ResponseEntity<>(trainerService.create(trainerRequestDto), HttpStatus.CREATED);
    }

    @ApiResponse(responseCode = "200", description = "Trainer is updated, returns update trainer")
    @ApiResponse(responseCode = "400", description = "Provided data to update trainer is not valid",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "401", description = "User is unauthorized",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "403", description = "User has not enough rights",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "404", description = "User with specified username to update not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @Operation(summary = "Update trainer", description = "Endpoint to update trainer profile")
    @PutMapping
    @TraceRequest
    public TrainerResponseDto updateTrainerProfile(
            @RequestBody @Valid TrainerForUpdateRequestDto updateTrainerRequestDto) {
        log.info("Start process of updating trainer with username '{}'", updateTrainerRequestDto.username());
        return trainerService.update(updateTrainerRequestDto.username(), updateTrainerRequestDto);
    }

    @ApiResponse(responseCode = "200", description = "Trainer status is updated successfully")
    @ApiResponse(responseCode = "401", description = "User is unauthorized",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "403", description = "User has not enough rights",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "404", description = "User with specified username to update not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @Operation(summary = "Update trainer status", description = "Endpoint to update trainer status by username")
    @PatchMapping("/{username}/status")
    @TraceRequest
    public void updateTrainerStatus(@PathVariable("username") String username,
                                    @RequestParam("active") boolean isActive) {
        log.info("Start process of updating status of trainer with username '{}'", username);
        if (isActive) {
            trainerService.activate(username);
        } else {
            trainerService.deactivate(username);
        }
    }
}
