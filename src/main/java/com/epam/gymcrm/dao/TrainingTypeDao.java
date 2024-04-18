package com.epam.gymcrm.dao;

import com.epam.gymcrm.model.TrainingType;

import java.util.List;
import java.util.Optional;

public interface TrainingTypeDao {
    Optional<TrainingType> getById(Long id);
    Optional<TrainingType> getByName(String name);
    List<TrainingType> getAll();
}
