package com.epam.gymcrm.rest;

import com.epam.gymcrm.dto.TrainerWorkloadRequestDto;
import com.epam.gymcrm.rest.feign.TrainingsReportServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "trainings-report-service", fallback = TrainingsReportServiceFallback.class)
public interface TrainingsReportService {
    @PostMapping("/api/trainer-workload")
    void updateTrainerWorkload(@RequestBody TrainerWorkloadRequestDto trainerWorkloadRequestDto);
}
