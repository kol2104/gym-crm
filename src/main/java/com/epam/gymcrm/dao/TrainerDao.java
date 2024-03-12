package com.epam.gymcrm.dao;

import com.epam.gymcrm.model.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerDao {
    Trainer save(Trainer t);
    List<Trainer> findAll();
    Optional<Trainer> findById(Long id);
    Trainer update(Trainer t);
    Optional<Trainer> findByFirstNameAndLastName(String firstName, String lastName);
}
