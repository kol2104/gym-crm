package com.epam.gymcrm.rest.feign;

import com.epam.gymcrm.dto.TrainerWorkloadRequestDto;
import com.epam.gymcrm.rest.TrainingsReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TrainingsReportServiceFallback implements TrainingsReportService {
    @Override
    public void updateTrainerWorkload(TrainerWorkloadRequestDto trainerWorkloadRequestDto) {
        log.warn("Training report service not available");
    }
}
