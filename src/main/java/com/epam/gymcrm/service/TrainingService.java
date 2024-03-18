package com.epam.gymcrm.service;

import com.epam.gymcrm.model.Training;

import java.util.List;

public interface TrainingService {
    Training create(Training training);
    List<Training> getAll();
    Training getById(Long id);
}
