package com.epam.gymcrm.service;

import com.epam.gymcrm.model.Trainee;

import java.util.List;

public interface TraineeService {
    Trainee create(Trainee trainee);
    List<Trainee> getAll();
    Trainee getById(Long id);
    void delete(Long id);
    Trainee update(Long id, Trainee trainee);

}
