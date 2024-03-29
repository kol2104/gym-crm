package com.epam.gymcrm.service;

import com.epam.gymcrm.model.Trainer;

import java.util.List;

public interface TrainerService {
    Trainer create(Trainer trainer);
    List<Trainer> getAll();
    Trainer getById(Long id);
    Trainer update(Long id, Trainer trainer);
}
