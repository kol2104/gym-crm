package com.epam.gymcrm.service;

import com.epam.gymcrm.model.Trainer;

import java.util.List;

public interface TrainerService {
    Trainer create(Trainer trainer);
    List<Trainer> getAll();
    Trainer getById(Long id);
    Trainer getByUsername(String username);
    boolean isUsernameAndPasswordValid(String username, String password);
    Trainer update(Long id, Trainer trainer);
    void updatePassword(Long id, String newPassword);
    void activate(Long id);
    void deactivate(Long id);
}
