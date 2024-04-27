package com.epam.gymcrm.dao;

import com.epam.gymcrm.model.Training;
import com.epam.gymcrm.model.TrainingCriteria;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TrainingDao {
    Training create(Training t);
    List<Training> getAll();
    Optional<Training> getById(Long id);
    List<Training> getTrainingsByCriteria(Map<TrainingCriteria, String> criteria);
}
