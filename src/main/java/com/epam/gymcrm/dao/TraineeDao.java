package com.epam.gymcrm.dao;

import com.epam.gymcrm.model.Trainee;
import com.epam.gymcrm.model.Trainer;

import java.util.List;
import java.util.Optional;

public interface TraineeDao {
    Trainee create(Trainee t);
    List<Trainer> getUnassignedOnTraineeTrainerListByUsername(String username);
    Optional<Trainee> getByUsername(String username);
    Trainee update(Trainee t);
    void delete(Long id);
    void deleteByUsername(String username);
}
