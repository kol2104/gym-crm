package com.epam.gymcrm.service.impl;

import com.epam.gymcrm.dao.TraineeDao;
import com.epam.gymcrm.exception.TraineeNotFoundException;
import com.epam.gymcrm.model.Trainee;
import com.epam.gymcrm.service.TraineeService;
import com.epam.gymcrm.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TraineeServiceImpl implements TraineeService {
    @Autowired
    private TraineeDao traineeDao;

    @Override
    public Trainee create(Trainee trainee) {
        log.info("Saving trainee: {}", trainee);
        String username = trainee.getFirstName() + "." + trainee.getLastName();
        trainee.setUsername(username);
        traineeDao.getByFirstNameAndLastName(trainee.getFirstName(), trainee.getLastName())
                .ifPresent(t -> trainee.setUsername(username + trainee.getId()));

        trainee.setPassword(PasswordUtil.getRandomPassword(10));
        return traineeDao.create(trainee);
    }

    @Override
    public List<Trainee> getAll() {
        log.info("Retrieving active trainees.");
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
    public void delete(Long id) {
        log.info("Deleting trainee with id: {}", id);
        traineeDao.delete(id);
    }

    @Override
    public Trainee update(Long id, Trainee trainee) {
        log.info("Updating trainee with id {}: {}", id, trainee);
        if (traineeDao.getById(id).isEmpty()) {
            log.error("Trainee with id {} not found.", id);
            throw new TraineeNotFoundException(id);
        }
        trainee.setId(id);
        return traineeDao.update(trainee);
    }
}
