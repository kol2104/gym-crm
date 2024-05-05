package com.epam.gymcrm.service.impl;

import com.epam.gymcrm.dao.TraineeDao;
import com.epam.gymcrm.dao.TrainerDao;
import com.epam.gymcrm.dao.TrainingDao;
import com.epam.gymcrm.dao.TrainingTypeDao;
import com.epam.gymcrm.dto.TrainerWorkloadRequestDto;
import com.epam.gymcrm.dto.training.TrainingDto;
import com.epam.gymcrm.dto.training.TrainingToDeleteRequestDto;
import com.epam.gymcrm.exception.TraineeNotFoundException;
import com.epam.gymcrm.exception.TrainerNotFoundException;
import com.epam.gymcrm.exception.TrainingDateConstraintViolation;
import com.epam.gymcrm.exception.TrainingTypeNotFoundException;
import com.epam.gymcrm.mapper.TrainingMapper;
import com.epam.gymcrm.model.Training;
import com.epam.gymcrm.model.TrainingCriteria;
import com.epam.gymcrm.message.TrainingsReportService;
import com.epam.gymcrm.service.TrainingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
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
    private final TrainingsReportService trainingsReportService;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional
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
        checkTrainingDateConstraint(training);
        log.info("Saving trainings: {}", training);
        trainingDao.create(training);
        trainingsReportService.updateTrainerWorkload(buildTrainerWorkloadRequestDto(training, true));
    }

    private void checkTrainingDateConstraint(Training training) {
        Map<TrainingCriteria, String> criteria = new EnumMap<>(TrainingCriteria.class);
        criteria.put(TrainingCriteria.FROM_DATE, training.getTrainingDate().format(formatter));
        criteria.put(TrainingCriteria.TO_DATE, training.getTrainingDate()
            .plusMinutes(training.getTrainingDuration()).format(formatter));
        criteria.put(TrainingCriteria.TRAINEE_USERNAME, training.getTrainee().getUsername());
        criteria.put(TrainingCriteria.TRAINER_USERNAME, training.getTrainer().getUsername());
        if (!trainingDao.getTrainingsByCriteria(criteria).isEmpty()) {
            log.error("Trainee or trainer already busy in this time: {}", training.getTrainingDate());
            throw new TrainingDateConstraintViolation(training.getTrainingDate());
        }
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

    @Transactional
    @Override
    public void delete(TrainingToDeleteRequestDto trainingToDeleteRequestDto) {
        log.info("Delete trainings by trainings date, trainer and trainee usernames");
        Map<TrainingCriteria, String> criteria = new EnumMap<>(TrainingCriteria.class);
        criteria.put(TrainingCriteria.FROM_DATE, trainingToDeleteRequestDto.trainingDate().format(formatter));
        criteria.put(TrainingCriteria.TO_DATE, trainingToDeleteRequestDto.trainingDate().format(formatter));
        criteria.put(TrainingCriteria.TRAINEE_USERNAME, trainingToDeleteRequestDto.traineeUsername());
        criteria.put(TrainingCriteria.TRAINER_USERNAME, trainingToDeleteRequestDto.trainerUsername());
        List<Training> trainings = trainingDao.getTrainingsByCriteria(criteria);
        for (Training training : trainings) {
            trainingDao.delete(training.getId());
            log.info("Training deleted successfully");
            trainingsReportService.updateTrainerWorkload(buildTrainerWorkloadRequestDto(training, false));
        }
    }

    private TrainerWorkloadRequestDto buildTrainerWorkloadRequestDto(Training training, boolean action) {
        return TrainerWorkloadRequestDto.builder()
            .username(training.getTrainer().getUsername())
            .firstname(training.getTrainer().getFirstName())
            .lastname(training.getTrainer().getLastName())
            .isActive(training.getTrainer().isActive())
            .trainingDate(training.getTrainingDate())
            .trainingDuration(training.getTrainingDuration())
            .action(action)
            .build();
    }
}
