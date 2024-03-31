package com.epam.gymcrm.service.impl;

import com.epam.gymcrm.dao.TrainerDao;
import com.epam.gymcrm.exception.TraineeNotFoundException;
import com.epam.gymcrm.exception.TrainerNotFoundException;
import com.epam.gymcrm.model.Trainee;
import com.epam.gymcrm.model.Trainer;
import com.epam.gymcrm.service.TrainerService;
import com.epam.gymcrm.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final TrainerDao trainerDao;

    @Override
    public Trainer create(Trainer trainer) {
        log.info("Saving trainer: {}", trainer);
        trainer.setUsername(getUsername(trainer.getFirstName(), trainer.getLastName()));
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
    public Trainer getByUsername(String username) {
        log.info("Finding trainer by username: {}", username);
        return trainerDao.getByUsername(username)
                .orElseThrow(() -> {
                    log.error("Trainer with username {} not found.", username);
                    return new TrainerNotFoundException(username);
                });
    }

    @Override
    public boolean isUsernameAndPasswordValid(String username, String password) {
        log.info("Finding trainer by username '{}' and password", username);
        return trainerDao.getByUsernameAndPassword(username, password).isPresent();
    }

    @Override
    public Trainer update(Long id, Trainer trainer) {
        log.info("Updating trainer with id {}: {}", id, trainer);
        Optional<Trainer> trainerFromDatabase = trainerDao.getById(id);
        if (trainerFromDatabase.isEmpty()) {
            log.error("Trainer with id {} not found.", id);
            throw new TrainerNotFoundException(id);
        }
        trainer.setId(id);
        if (trainer.getUsername() == null) {
            trainer.setUsername(getUsername(trainer.getFirstName(), trainer.getLastName()));
        }
        trainer.setPassword(trainerFromDatabase.get().getPassword());
        return trainerDao.update(trainer);
    }

    @Override
    public void updatePassword(Long id, String newPassword) {
        log.info("Updating trainer's password with id {}", id);
        if (newPassword == null) {
            log.warn("Passed new password is null");
        }
        Trainer foundTrainee = trainerDao.getById(id).orElseThrow(() -> {
            log.error("Trainer with id {} not found.", id);
            return new TrainerNotFoundException(id);
        });
        foundTrainee.setPassword(newPassword);
        trainerDao.update(foundTrainee);
    }

    @Override
    public void activate(Long id) {
        log.info("Activating trainer with id {}", id);
        Trainer foundTrainee = trainerDao.getById(id).orElseThrow(() -> {
            log.error("Trainer with id {} not found.", id);
            return new TrainerNotFoundException(id);
        });
        if (foundTrainee.isActive()) {
            log.debug("Trainer already is activated");
            return;
        }
        foundTrainee.setActive(true);
        trainerDao.update(foundTrainee);
    }

    @Override
    public void deactivate(Long id) {
        log.info("Deactivating trainer with id {}", id);
        Trainer foundTrainee = trainerDao.getById(id).orElseThrow(() -> {
            log.error("Trainer with id {} not found.", id);
            return new TrainerNotFoundException(id);
        });
        if (!foundTrainee.isActive()) {
            log.debug("Trainer already is deactivated");
            return;
        }
        foundTrainee.setActive(false);
        trainerDao.update(foundTrainee);
    }

    private String getUsername(String firstName, String lastName) {
        String username = firstName + "." + lastName;
        if (trainerDao.getByFirstNameAndLastName(firstName, lastName).isPresent()) {
            return username + "_" + System.currentTimeMillis();
        }
        return username;
    }
}
