package com.epam.gymcrm.service;

import com.epam.gymcrm.model.Trainer;

import java.util.List;

public interface TrainerService {
    Trainer save(Trainer trainer);
    List<Trainer> findAll();
    Trainer findById(Long id);
    Trainer update(Long id, Trainer trainer);
}
