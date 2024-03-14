package com.epam.gymcrm.dao;

import com.epam.gymcrm.model.Trainee;

import java.util.List;
import java.util.Optional;

public interface TraineeDao {
    Trainee create(Trainee t);
    List<Trainee> getAll();
    Optional<Trainee> getById(Long id);
    Trainee update(Trainee t);
    void delete(Long id);
    Optional<Trainee> getByFirstNameAndLastName(String firstName, String lastName);
}
