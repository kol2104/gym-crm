package com.epam.gymcrm.service;

import com.epam.gymcrm.dto.user.UserCredentialsDto;
import com.epam.gymcrm.dto.trainer.TrainerForUpdateRequestDto;
import com.epam.gymcrm.dto.trainer.TrainerRequestDto;
import com.epam.gymcrm.dto.trainer.TrainerResponseDto;

public interface TrainerService {
    UserCredentialsDto create(TrainerRequestDto trainerRequestDto);
    TrainerResponseDto getByUsername(String username);
    TrainerResponseDto update(String username, TrainerForUpdateRequestDto updateTrainerRequestDto);
    void activate(String username);
    void deactivate(String username);
}
