package com.epam.gymcrm.service.impl;

import com.epam.gymcrm.dao.TrainerDao;
import com.epam.gymcrm.exception.TrainerNotFoundException;
import com.epam.gymcrm.model.Trainer;
import com.epam.gymcrm.service.TrainerService;
import com.epam.gymcrm.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TrainerServiceImpl implements TrainerService {
    @Autowired
    private TrainerDao trainerDao;

    @Override
    public Trainer create(Trainer trainer) {
        log.info("Saving trainer: {}", trainer);
        String username = trainer.getFirstName() + "." + trainer.getLastName();
        trainer.setUsername(username);
        trainerDao.getByFirstNameAndLastName(trainer.getFirstName(), trainer.getLastName())
                .ifPresent(t -> trainer.setUsername(username + trainer.getId()));

        trainer.setPassword(PasswordUtil.getRandomPassword(10));
        return trainerDao.create(trainer);
    }

    @Override
    public List<Trainer> getAll() {
        log.info("Retrieving all trainers.");
        return trainerDao.getAll();
    }

    @Override
    public Trainer getById(Long id) {
        log.info("Finding trainer by id: {}", id);
        return trainerDao.getById(id)
                .orElseThrow(() -> {
                    log.error("Trainer with id {} not found.", id);
                    return new TrainerNotFoundException(id);
                });
    }

    @Override
    public Trainer update(Long id, Trainer trainer) {
        log.info("Updating trainer with id {}: {}", id, trainer);
        if (trainerDao.getById(id).isEmpty()) {
            log.error("Trainer with id {} not found.", id);
            throw new TrainerNotFoundException(id);
        }
        trainer.setId(id);
        return trainerDao.update(trainer);
    }
}
