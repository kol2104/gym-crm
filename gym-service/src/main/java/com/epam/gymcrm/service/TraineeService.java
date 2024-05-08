package com.epam.gymcrm.service;

import com.epam.gymcrm.dto.trainee.TraineeForUpdateRequestDto;
import com.epam.gymcrm.dto.trainee.TraineeRequestDto;
import com.epam.gymcrm.dto.trainee.TraineeResponseDto;
import com.epam.gymcrm.dto.trainer.PlainTrainerResponseDto;
import com.epam.gymcrm.dto.trainer.TrainerUsernameDto;
import com.epam.gymcrm.dto.user.UserCredentialsDto;

import java.util.List;

public interface TraineeService {
    UserCredentialsDto create(TraineeRequestDto traineeRequestDto);
    TraineeResponseDto getByUsername(String username);
    void deleteByUsername(String username);
    TraineeResponseDto update(String username, TraineeForUpdateRequestDto updateTraineeRequestDto);
    void updateTrainersList(String username, List<TrainerUsernameDto> trainerUsernameDtos);
    List<PlainTrainerResponseDto> getUnassignedOnTraineeTrainerListByUsername(String username);
    void activate(String username);
    void deactivate(String username);

}
