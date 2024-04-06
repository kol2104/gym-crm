package com.epam.gymcrm.service;

import com.epam.gymcrm.model.Training;
import com.epam.gymcrm.model.TrainingCriteria;

import java.util.List;
import java.util.Map;

public interface TrainingService {
    Training create(Training training);
    List<Training> getAll();
    Training getById(Long id);
    List<Training> getTrainingByTraineeUsernameAndCriteria(String username, Map<TrainingCriteria, Object> criteria);
    List<Training> getTrainingByTrainerUsernameAndCriteria(String username, Map<TrainingCriteria, Object> criteria);
}
