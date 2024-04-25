package com.epam.gymcrm.controller;

import com.epam.gymcrm.aspect.annotation.TraceRequest;
import com.epam.gymcrm.dto.trainee.TraineeForUpdateRequestDto;
import com.epam.gymcrm.dto.trainee.TraineeRequestDto;
import com.epam.gymcrm.dto.trainee.TraineeResponseDto;
import com.epam.gymcrm.dto.trainer.PlainTrainerResponseDto;
import com.epam.gymcrm.dto.trainer.TrainerUsernameDto;
import com.epam.gymcrm.dto.user.UserCredentialsDto;
import com.epam.gymcrm.exception.model.ExceptionResponse;
import com.epam.gymcrm.service.TraineeService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/trainees")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Trainee controller", description = "Trainee user operations")
public class TraineeController {

    private final TraineeService traineeService;

    @ApiResponse(responseCode = "200", description = "Return trainee profile")
    @ApiResponse(responseCode = "401", description = "User is unauthorized",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "403", description = "User has not enough rights",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @Operation(summary = "Get trainee profile", description = "Get trainee profile with specified username")
    @GetMapping("/{username}")
    @TraceRequest
    public TraineeResponseDto getTraineeProfile(@PathVariable("username") String username) {
        log.info("Start process of getting trainee with username '{}'", username);
        return traineeService.getByUsername(username);
    }

    @ApiResponse(responseCode = "201", description = "Return credentials of created new trainee user")
    @ApiResponse(responseCode = "400", description = "Provided trainee to save is not valid",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @Operation(summary = "Create new trainee", description = "Create new trainee and return credentials of new trainee")
    @PostMapping
    @TraceRequest
    public ResponseEntity<UserCredentialsDto> createTrainee(@RequestBody @Valid TraineeRequestDto traineeRequestDto) {
        log.info("Start process of saving new trainee");
        return new ResponseEntity<>(traineeService.create(traineeRequestDto), HttpStatus.CREATED);
    }

    @ApiResponse(responseCode = "200", description = "Trainee is updated, returns update trainee")
    @ApiResponse(responseCode = "400", description = "Provided data to update trainee is not valid",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "401", description = "User is unauthorized",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "403", description = "User has not enough rights",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "404", description = "User with specified username to update not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @Operation(summary = "Update trainee", description = "Endpoint to update trainee profile")
    @PutMapping
    @TraceRequest
    public TraineeResponseDto updateTraineeProfile(
            @RequestBody @Valid TraineeForUpdateRequestDto updateTraineeRequestDto) {
        log.info("Start process of updating trainee with username '{}'", updateTraineeRequestDto.username());
        return traineeService.update(updateTraineeRequestDto.username(), updateTraineeRequestDto);
    }

    @ApiResponse(responseCode = "200", description = "Trainee deleted successfully")
    @ApiResponse(responseCode = "401", description = "User is unauthorized",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "403", description = "User has not enough rights",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @Operation(summary = "Delete trainee", description = "Endpoint to delete trainee")
    @DeleteMapping("/{username}")
    @TraceRequest
    public void deleteTraineeProfile(@PathVariable("username") String username) {
        log.info("Start process of deleting trainee with username '{}'", username);
        traineeService.deleteByUsername(username);
    }

    @ApiResponse(responseCode = "200", description = "Return list of unassigned to provided trainee trainers")
    @ApiResponse(responseCode = "401", description = "User is unauthorized",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "403", description = "User has not enough rights",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "404", description = "User with specified username not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @Operation(summary = "Get unassigned trainers list", description = "Endpoint to get unassigned to trainee trainers list")
    @GetMapping("/{username}/unassigned-trainers")
    @TraceRequest
    public List<PlainTrainerResponseDto> getUnassignedTrainers(@PathVariable("username") String username) {
        log.info("Start process of getting unassigned trainers with trainee username '{}'", username);
        return traineeService.getUnassignedOnTraineeTrainerListByUsername(username);
    }

    @ApiResponse(responseCode = "200", description = "Trainer list for trainee is updated successfully")
    @ApiResponse(responseCode = "400", description = "Provided data to update trainer list is not valid",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "401", description = "User is unauthorized",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "403", description = "User has not enough rights",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "404", description = "User with specified username to update not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @Operation(summary = "Update trainer list for trainee", description = "Endpoint to update trainer list for trainee by username")
    @PutMapping("/{username}/trainer-list")
    @TraceRequest
    public void updateTrainerList(@PathVariable("username") String username,
                                  @RequestBody @Valid List<TrainerUsernameDto> trainerUsernameDtos) {
        log.info("Start process of updating trainer list for trainee with username '{}'", username);
        traineeService.updateTrainersList(username, trainerUsernameDtos);
    }

    @ApiResponse(responseCode = "200", description = "Trainee status is updated successfully")
    @ApiResponse(responseCode = "401", description = "User is unauthorized",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "403", description = "User has not enough rights",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "404", description = "User with specified username to update not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @Operation(summary = "Update trainee status", description = "Endpoint to update trainee status by username")
    @PatchMapping("/{username}/status")
    @TraceRequest
    public void updateTraineeStatus(@PathVariable("username") String username,
                                    @RequestParam("active") boolean isActive) {
        log.info("Start process of updating status of trainee with username '{}'", username);
        if (isActive) {
            traineeService.activate(username);
        } else {
            traineeService.deactivate(username);
        }
    }
}
