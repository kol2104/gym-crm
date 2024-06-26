package com.epam.gymcrm.dao;

import com.epam.gymcrm.model.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerDao {
    Trainer create(Trainer t);
    List<Trainer> getAllByUsernames(List<String> usernames);
    Optional<Trainer> getByUsername(String username);
    Trainer update(Trainer t);
}
