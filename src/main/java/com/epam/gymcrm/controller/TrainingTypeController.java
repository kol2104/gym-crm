package com.epam.gymcrm.controller;

import com.epam.gymcrm.aspect.annotation.TraceRequest;
import com.epam.gymcrm.dto.TrainingTypeDto;
import com.epam.gymcrm.exception.model.ExceptionResponse;
import com.epam.gymcrm.service.TrainingTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/training-types")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Training type controller", description = "Training type operations")
public class TrainingTypeController {

    private final TrainingTypeService trainingTypeService;

    @ApiResponse(responseCode = "200", description = "Return all possible training types")
    @ApiResponse(responseCode = "401", description = "User is unauthorized",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "403", description = "User has not enough rights",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @Operation(summary = "Get all training types", description = "Get all possible training types")
    @GetMapping
    @TraceRequest
    public List<TrainingTypeDto> getAll() {
        log.info("Start process of getting list of training types");
        return trainingTypeService.getAll();
    }
}
