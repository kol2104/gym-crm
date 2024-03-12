package com.epam.gymcrm.service.impl;

import com.epam.gymcrm.dao.TraineeDao;
import com.epam.gymcrm.exception.TraineeNotFoundException;
import com.epam.gymcrm.model.Trainee;
import com.epam.gymcrm.service.TraineeService;
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
    public Trainee save(Trainee trainee) {
        log.info("Saving trainee: {}", trainee);
        return traineeDao.save(trainee);
    }

    @Override
    public List<Trainee> findAll() {
        log.info("Retrieving active trainees.");
        return traineeDao.findAll();
    }

    @Override
    public Trainee findById(Long id) {
        log.info("Finding trainee by id: {}", id);
        return traineeDao.findById(id)
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
        if (traineeDao.findById(id).isEmpty()) {
            log.error("Trainee with id {} not found.", id);
            throw new TraineeNotFoundException(id);
        }
        trainee.setId(id);
        return traineeDao.update(trainee);
    }
}
