package com.epam.gymcrm.dao;

import com.epam.gymcrm.model.Training;

import java.util.List;
import java.util.Optional;

public interface TrainingDao {
    Training save(Training t);
    List<Training> findAll();
    Optional<Training> findById(Long id);
}
