package com.epam.gymcrm.service.impl;

import com.epam.gymcrm.dao.TrainerDao;
import com.epam.gymcrm.exception.TrainerNotFoundException;
import com.epam.gymcrm.model.Trainer;
import com.epam.gymcrm.service.TrainerService;
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
    public Trainer save(Trainer trainer) {
        log.info("Saving trainer: {}", trainer);
        return trainerDao.save(trainer);
    }

    @Override
    public List<Trainer> findAll() {
        log.info("Retrieving all trainers.");
        return trainerDao.findAll();
    }

    @Override
    public Trainer findById(Long id) {
        log.info("Finding trainer by id: {}", id);
        return trainerDao.findById(id)
                .orElseThrow(() -> {
                    log.error("Trainer with id {} not found.", id);
                    return new TrainerNotFoundException(id);
                });
    }

    @Override
    public Trainer update(Long id, Trainer trainer) {
        log.info("Updating trainer with id {}: {}", id, trainer);
        if (trainerDao.findById(id).isEmpty()) {
            log.error("Trainer with id {} not found.", id);
            throw new TrainerNotFoundException(id);
        }
        trainer.setId(id);
        return trainerDao.update(trainer);
    }
}
