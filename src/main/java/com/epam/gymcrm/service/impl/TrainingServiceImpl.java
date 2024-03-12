package com.epam.gymcrm.service.impl;

import com.epam.gymcrm.dao.TraineeDao;
import com.epam.gymcrm.dao.TrainerDao;
import com.epam.gymcrm.dao.TrainingDao;
import com.epam.gymcrm.exception.TraineeNotFoundException;
import com.epam.gymcrm.exception.TrainerNotFoundException;
import com.epam.gymcrm.exception.TrainingNotFoundException;
import com.epam.gymcrm.model.Training;
import com.epam.gymcrm.service.TrainingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TrainingServiceImpl implements TrainingService {
    @Autowired
    private TrainingDao trainingDao;
    @Autowired
    private TrainerDao trainerDao;
    @Autowired
    private TraineeDao traineeDao;

    @Override
    public Training save(Training training) {
        if (traineeDao.findById(training.getTraineeId()).isEmpty()) {
            log.error("Trainee with id {} not found.", training.getTraineeId());
            throw new TraineeNotFoundException(training.getTraineeId());
        }
        if (trainerDao.findById(training.getTrainerId()).isEmpty()) {
            log.error("Trainer with id {} not found.", training.getTrainerId());
            throw new TrainerNotFoundException(training.getTrainerId());
        }
        log.info("Saving training: {}", training);
        return trainingDao.save(training);
    }

    @Override
    public List<Training> findAll() {
        log.info("Retrieving all trainings.");
        return trainingDao.findAll();
    }

    @Override
    public Training findById(Long id) {
        log.info("Finding training by id: {}", id);
        return trainingDao.findById(id)
                .orElseThrow(() -> {
                    log.error("Training with id {} not found.", id);
                    return new TrainingNotFoundException(id);
                });
    }
}
