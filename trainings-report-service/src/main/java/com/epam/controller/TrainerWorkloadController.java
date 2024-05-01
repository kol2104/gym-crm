package com.epam.controller;

import com.epam.aspect.annotation.TraceRequest;
import com.epam.dto.TrainerWorkloadRequestDto;
import com.epam.dto.TrainerWorkloadResponseDto;
import com.epam.exception.model.ExceptionResponse;
import com.epam.service.TrainerWorkloadService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trainer-workload")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Trainer workload controller", description = "Trainer workload operations")
public class TrainerWorkloadController {

    private final TrainerWorkloadService trainerWorkloadService;

    @ApiResponse(responseCode = "200", description = "Trainer workload created or updated")
    @ApiResponse(responseCode = "400", description = "Provided data is not valid",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "401", description = "User is unauthorized",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @Operation(summary = "Create or update trainer workload", description = "Create or update trainer workload")
    @TraceRequest
    @PostMapping
    public void updateTrainerWorkload(@RequestBody @Valid TrainerWorkloadRequestDto trainerWorkloadRequestDto) {
        log.info("Start process of adding or deleting trainer workload with username: {}", trainerWorkloadRequestDto.username());
        trainerWorkloadService.updateTrainerWorkload(trainerWorkloadRequestDto);
    }

    @ApiResponse(responseCode = "200", description = "Return trainer workload")
    @ApiResponse(responseCode = "401", description = "User is unauthorized",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @Operation(summary = "Get trainer workload", description = "Get trainer workload by trainer username")
    @TraceRequest
    @GetMapping("/{username}")
    public TrainerWorkloadResponseDto getTrainerWorkload(@PathVariable("username") String trainerUsername) {
        log.info("Start process of getting trainer workload with username: {}", trainerUsername);
        return trainerWorkloadService.getTrainerWorkload(trainerUsername);
    }
}
