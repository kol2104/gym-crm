package com.epam.gymcrm.service.impl;

import com.epam.gymcrm.dao.TraineeDao;
import com.epam.gymcrm.dao.TrainerDao;
import com.epam.gymcrm.dao.TrainingDao;
import com.epam.gymcrm.dao.TrainingTypeDao;
import com.epam.gymcrm.dto.training.TrainingDto;
import com.epam.gymcrm.exception.TraineeNotFoundException;
import com.epam.gymcrm.exception.TrainerNotFoundException;
import com.epam.gymcrm.exception.TrainingTypeNotFoundException;
import com.epam.gymcrm.mapper.TrainingMapper;
import com.epam.gymcrm.model.Training;
import com.epam.gymcrm.model.TrainingCriteria;
import com.epam.gymcrm.service.TrainingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private final TrainingDao trainingDao;
    private final TrainerDao trainerDao;
    private final TraineeDao traineeDao;
    private final TrainingTypeDao trainingTypeDao;
    private final TrainingMapper trainingMapper;

    @Override
    public void create(TrainingDto trainingDto) {
        Training training = trainingMapper.dtoToModel(trainingDto);
        training.setTrainee(traineeDao.getByUsername(training.getTrainee().getUsername()).orElseThrow(() -> {
            log.error("Trainee with username {} not found.", training.getTrainee().getUsername());
            return new TraineeNotFoundException(training.getTrainee().getUsername());
        }));
        training.setTrainer(trainerDao.getByUsername(training.getTrainer().getUsername()).orElseThrow(() -> {
            log.error("Trainer with username {} not found.", training.getTrainer().getUsername());
            return new TrainerNotFoundException(training.getTrainer().getUsername());
        }));
        training.setTrainingType(trainingTypeDao.getByName(training.getTrainingType().getName()).orElseThrow(() -> {
            log.error("Training type with name {} not found.", training.getTrainingType().getName());
            return new TrainingTypeNotFoundException(training.getTrainingType().getName());
        }));
        log.info("Saving training: {}", training);
        trainingDao.create(training);
    }

    @Override
    public List<TrainingDto> getTrainingByTraineeUsernameAndCriteria(String username, Map<TrainingCriteria, String> criteria) {
        log.info("Finding trainings by trainee username '{}' and criteria", username);
        if (criteria == null) {
            criteria = new EnumMap<>(TrainingCriteria.class);
        }
        criteria.put(TrainingCriteria.TRAINEE_USERNAME, username);
        return trainingDao.getTrainingsByCriteria(criteria).stream()
                .map(trainingMapper::modelToDto)
                .toList();
    }

    @Override
    public List<TrainingDto> getTrainingByTrainerUsernameAndCriteria(String username, Map<TrainingCriteria, String> criteria) {
        log.info("Finding trainings by trainer username '{}' and criteria", username);

        if (criteria == null) {
            criteria = new EnumMap<>(TrainingCriteria.class);
        }
        criteria.put(TrainingCriteria.TRAINER_USERNAME, username);
        return trainingDao.getTrainingsByCriteria(criteria).stream()
                .map(trainingMapper::modelToDto)
                .toList();
    }
}
