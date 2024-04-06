package com.epam.gymcrm.dao;

import com.epam.gymcrm.model.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerDao {
    Trainer create(Trainer t);
    List<Trainer> getAll();
    Optional<Trainer> getById(Long id);
    Optional<Trainer> getByUsername(String username);
    Optional<Trainer> getByUsernameAndPassword(String username, String password);
    Trainer update(Trainer t);
    Optional<Trainer> getByFirstNameAndLastName(String firstName, String lastName);
}
