package com.epam.gymcrm.service;

import com.epam.gymcrm.dto.training.TrainingDto;
import com.epam.gymcrm.model.TrainingCriteria;

import java.util.List;
import java.util.Map;

public interface TrainingService {
    void create(TrainingDto trainingDto);
    List<TrainingDto> getTrainingByTraineeUsernameAndCriteria(String username, Map<TrainingCriteria, String> criteria);
    List<TrainingDto> getTrainingByTrainerUsernameAndCriteria(String username, Map<TrainingCriteria, String> criteria);
}
