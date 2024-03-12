package com.epam.gymcrm.service;

import com.epam.gymcrm.model.Trainee;

import java.util.List;

public interface TraineeService {
    Trainee save(Trainee trainee);
    List<Trainee> findAll();
    Trainee findById(Long id);
    void delete(Long id);
    Trainee update(Long id, Trainee trainee);

}
