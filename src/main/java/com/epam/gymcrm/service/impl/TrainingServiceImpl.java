package com.epam.gymcrm.service.impl;

import com.epam.gymcrm.dao.TraineeDao;
import com.epam.gymcrm.dao.TrainerDao;
import com.epam.gymcrm.dao.TrainingDao;
import com.epam.gymcrm.exception.TraineeNotFoundException;
import com.epam.gymcrm.exception.TrainerNotFoundException;
import com.epam.gymcrm.exception.TrainingNotFoundException;
import com.epam.gymcrm.model.Training;
import com.epam.gymcrm.model.TrainingCriteria;
import com.epam.gymcrm.service.TrainingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private final TrainingDao trainingDao;

    private final TrainerDao trainerDao;

    private final TraineeDao traineeDao;

    @Override
    public Training create(Training training) {
        if (traineeDao.getById(training.getTrainee().getId()).isEmpty()) {
            log.error("Trainee with id {} not found.", training.getTrainee().getId());
            throw new TraineeNotFoundException(training.getTrainee().getId());
        }
        if (trainerDao.getById(training.getTrainer().getId()).isEmpty()) {
            log.error("Trainer with id {} not found.", training.getTrainer().getId());
            throw new TrainerNotFoundException(training.getTrainer().getId());
        }
        log.info("Saving training: {}", training);
        return trainingDao.create(training);
    }

    @Override
    public List<Training> getAll() {
        log.info("Retrieving all trainings.");
        return trainingDao.getAll();
    }

    @Override
    public Training getById(Long id) {
        log.info("Finding training by id: {}", id);
        return trainingDao.getById(id)
                .orElseThrow(() -> {
                    log.error("Training with id {} not found.", id);
                    return new TrainingNotFoundException(id);
                });
    }

    @Override
    public List<Training> getTrainingByTraineeUsernameAndCriteria(String username, Map<TrainingCriteria, Object> criteria) {
        log.info("Finding trainings by trainee username '{}' and criteria", username);
        if (criteria == null) {
            criteria = new EnumMap<>(TrainingCriteria.class);
        }
        criteria.put(TrainingCriteria.TRAINEE_USERNAME, username);
        return trainingDao.getTrainingsByCriteria(criteria);
    }

    @Override
    public List<Training> getTrainingByTrainerUsernameAndCriteria(String username, Map<TrainingCriteria, Object> criteria) {
        log.info("Finding trainings by trainer username '{}' and criteria", username);

        if (criteria == null) {
            criteria = new EnumMap<>(TrainingCriteria.class);
        }
        criteria.put(TrainingCriteria.TRAINER_USERNAME, username);
        return trainingDao.getTrainingsByCriteria(criteria);
    }
}
