package com.epam.gymcrm.service.impl;

import com.epam.gymcrm.dao.TraineeDao;
import com.epam.gymcrm.exception.TraineeNotFoundException;
import com.epam.gymcrm.model.Trainee;
import com.epam.gymcrm.model.Trainer;
import com.epam.gymcrm.service.TraineeService;
import com.epam.gymcrm.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TraineeServiceImpl implements TraineeService {

    private final TraineeDao traineeDao;

    @Override
    public Trainee create(Trainee trainee) {
        log.info("Saving trainee: {}", trainee);
        trainee.setUsername(getUsername(trainee.getFirstName(), trainee.getLastName()));
        trainee.setPassword(PasswordUtil.getRandomPassword(10));
        return traineeDao.create(trainee);
    }

    @Override
    public List<Trainee> getAll() {
        log.info("Retrieving all trainees.");
        return traineeDao.getAll();
    }

    @Override
    public Trainee getById(Long id) {
        log.info("Finding trainee by id: {}", id);
        return traineeDao.getById(id)
                .orElseThrow(() -> {
                    log.error("Trainee with id {} not found.", id);
                    return new TraineeNotFoundException(id);
                });
    }

    @Override
    public Trainee getByUsername(String username) {
        log.info("Finding trainee by username: {}", username);
        return traineeDao.getByUsername(username)
                .orElseThrow(() -> {
                    log.error("Trainee with username {} not found.", username);
                    return new TraineeNotFoundException(username);
                });
    }

    @Override
    public boolean isUsernameAndPasswordValid(String username, String password) {
        log.info("Finding trainee by username '{}' and password", username);
        return traineeDao.getByUsernameAndPassword(username, password).isPresent();
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting trainee with id: {}", id);
        traineeDao.delete(id);
    }

    @Override
    public void deleteByUsername(String username) {
        log.info("Deleting trainee with username: {}", username);
        traineeDao.deleteByUsername(username);
    }

    @Override
    public Trainee update(Long id, Trainee trainee) {
        log.info("Updating trainee with id {}", id);
        Optional<Trainee> traineeFromDatabase = traineeDao.getById(id);
        if (traineeFromDatabase.isEmpty()) {
            log.error("Trainee with id {} not found.", id);
            throw new TraineeNotFoundException(id);
        }
        trainee.setId(id);
        if (trainee.getUsername() == null) {
            trainee.setUsername(getUsername(trainee.getFirstName(), trainee.getLastName()));
        }
        trainee.setPassword(traineeFromDatabase.get().getPassword());
        return traineeDao.update(trainee);
    }

    @Override
    public void updatePassword(Long id, String newPassword) {
        log.info("Updating trainee's password with id {}", id);
        if (newPassword == null) {
            log.warn("Passed new password is null");
        }
        Trainee foundTrainee = traineeDao.getById(id).orElseThrow(() -> {
            log.error("Trainee with id {} not found.", id);
            return new TraineeNotFoundException(id);
        });
        foundTrainee.setPassword(newPassword);
        traineeDao.update(foundTrainee);
    }

    @Override
    public void updateTrainersList(Long id, List<Trainer> trainers) {
        log.info("Updating trainee's trainers list with id {}", id);
        Trainee foundTrainee = traineeDao.getById(id).orElseThrow(() -> {
            log.error("Trainee with id {} not found.", id);
            return new TraineeNotFoundException(id);
        });
        foundTrainee.setTrainers(trainers);
        traineeDao.update(foundTrainee);
    }

    @Override
    public List<Trainer> getUnassignedOnTraineeTrainerListByUsername(String username) {
        log.info("Retrieving trainers list that not assigned on trainee by trainee's username.");
        return traineeDao.getUnassignedOnTraineeTrainerListByUsername(username);
    }

    @Override
    public void activate(Long id) {
        log.info("Activating trainee with id {}", id);
        Trainee foundTrainee = traineeDao.getById(id).orElseThrow(() -> {
            log.error("Trainee with id {} not found.", id);
            return new TraineeNotFoundException(id);
        });
        if (foundTrainee.isActive()) {
            log.debug("Trainee already is activated");
            return;
        }
        foundTrainee.setActive(true);
        traineeDao.update(foundTrainee);
    }

    @Override
    public void deactivate(Long id) {
        log.info("Deactivating trainee with id {}", id);
        Trainee foundTrainee = traineeDao.getById(id).orElseThrow(() -> {
            log.error("Trainee with id {} not found.", id);
            return new TraineeNotFoundException(id);
        });
        if (!foundTrainee.isActive()) {
            log.debug("Trainee already is deactivated");
            return;
        }
        foundTrainee.setActive(false);
        traineeDao.update(foundTrainee);
    }

    private String getUsername(String firstName, String lastName) {
        String username = firstName + "." + lastName;
        if (traineeDao.getByFirstNameAndLastName(firstName, lastName).isPresent()) {
            return username + "_" + System.currentTimeMillis();
        }
        return username;
    }
}
