package com.epam.gymcrm.service;

import com.epam.gymcrm.model.Trainee;
import com.epam.gymcrm.model.Trainer;

import java.util.List;

public interface TraineeService {
    Trainee create(Trainee trainee);
    List<Trainee> getAll();
    Trainee getById(Long id);
    Trainee getByUsername(String username);
    boolean isUsernameAndPasswordValid(String username, String password);
    void delete(Long id);
    void deleteByUsername(String username);
    Trainee update(Long id, Trainee trainee);
    void updatePassword(Long id, String newPassword);
    void updateTrainersList(Long id, List<Trainer> trainers);
    List<Trainer> getUnassignedOnTraineeTrainerListByUsername(String username);
    void activate(Long id);
    void deactivate(Long id);

}
