package com.epam.gymcrm.dao;

import com.epam.gymcrm.model.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerDao {
    Trainer create(Trainer t);
    List<Trainer> getAll();
    Optional<Trainer> getById(Long id);
    Trainer update(Trainer t);
    Optional<Trainer> getByFirstNameAndLastName(String firstName, String lastName);
}
