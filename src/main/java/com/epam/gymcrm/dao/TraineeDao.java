package com.epam.gymcrm.dao;

import com.epam.gymcrm.model.Trainee;

import java.util.List;
import java.util.Optional;

public interface TraineeDao {
    Trainee save(Trainee t);
    List<Trainee> findAll();
    Optional<Trainee> findById(Long id);
    Trainee update(Trainee t);
    void delete(Long id);
    Optional<Trainee> findByFirstNameAndLastName(String firstName, String lastName);
}
