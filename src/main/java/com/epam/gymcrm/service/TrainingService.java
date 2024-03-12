package com.epam.gymcrm.service;

import com.epam.gymcrm.model.Training;

import java.util.List;

public interface TrainingService {
    Training save(Training training);
    List<Training> findAll();
    Training findById(Long id);
}
