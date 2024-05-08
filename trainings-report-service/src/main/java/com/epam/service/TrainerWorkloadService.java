package com.epam.service;

import com.epam.dto.TrainerWorkloadRequestDto;
import com.epam.dto.TrainerWorkloadResponseDto;

public interface TrainerWorkloadService {
    void updateTrainerWorkload(TrainerWorkloadRequestDto trainerWorkloadRequestDto);

    TrainerWorkloadResponseDto getTrainerWorkload(String trainerUsername);
}
