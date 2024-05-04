package com.epam.gymcrm.message;

import com.epam.gymcrm.dto.TrainerWorkloadRequestDto;

public interface TrainingsReportService {
    void updateTrainerWorkload(TrainerWorkloadRequestDto trainerWorkloadRequestDto);
}
