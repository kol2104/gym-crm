package com.epam.gymcrm.controller;

import com.epam.gymcrm.aspect.annotation.TraceRequest;
import com.epam.gymcrm.auth.annotation.Authenticated;
import com.epam.gymcrm.dto.training.TrainingDto;
import com.epam.gymcrm.exception.model.ExceptionResponse;
import com.epam.gymcrm.model.Role;
import com.epam.gymcrm.model.TrainingCriteria;
import com.epam.gymcrm.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trainings")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Training controller", description = "Training operations")
public class TrainingController {

    private final TrainingService trainingService;

    @ApiResponse(responseCode = "200", description = "Return trainee trainings")
    @ApiResponse(responseCode = "401", description = "User is unauthorized",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "403", description = "User has not enough rights",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @Operation(summary = "Get trainee trainings", description = "Get trainee trainings list")
    @GetMapping("/trainee/{username}")
    @Authenticated(roles = {Role.TRAINEE, Role.TRAINER})
    @TraceRequest
    public List<TrainingDto> getTraineeTrainings(@PathVariable("username") String username,
                                                @RequestBody(required = false) Map<TrainingCriteria, String> criteria,
                                                @RequestHeader(name = "Authorization", required = false) String token) {
        log.info("Start process of getting trainings of trainee  with username '{}'", username);
        return trainingService.getTrainingByTraineeUsernameAndCriteria(username, criteria);
    }

    @ApiResponse(responseCode = "200", description = "Return trainer trainings")
    @ApiResponse(responseCode = "401", description = "User is unauthorized",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "403", description = "User has not enough rights",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @Operation(summary = "Get trainer trainings", description = "Get trainer trainings")
    @GetMapping("/trainer/{username}")
    @Authenticated(roles = {Role.TRAINEE, Role.TRAINER})
    @TraceRequest
    public List<TrainingDto> getTrainerTrainings(@PathVariable("username") String username,
                                                @RequestBody(required = false) Map<TrainingCriteria, String> criteria,
                                                @RequestHeader(name = "Authorization", required = false) String token) {
        log.info("Start process of getting trainings of trainee with username '{}'", username);
        return trainingService.getTrainingByTrainerUsernameAndCriteria(username, criteria);
    }

    @ApiResponse(responseCode = "201", description = "Training created successfully")
    @ApiResponse(responseCode = "400", description = "Provided trainee to save is not valid",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "401", description = "User is unauthorized",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "403", description = "User has not enough rights",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @Operation(summary = "Create new trainee", description = "Create new trainee and return credentials of new trainee")
    @PostMapping
    @Authenticated(roles = {Role.TRAINEE, Role.TRAINER})
    @TraceRequest
    public void createTrainee(@RequestBody @Valid TrainingDto trainingDto) {
        log.info("Start process of saving new training");
        trainingService.create(trainingDto);
    }
}
