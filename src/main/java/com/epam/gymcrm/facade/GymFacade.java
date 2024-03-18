package com.epam.gymcrm.facade;

import com.epam.gymcrm.model.Trainee;
import com.epam.gymcrm.model.Trainer;
import com.epam.gymcrm.model.Training;
import com.epam.gymcrm.service.TraineeService;
import com.epam.gymcrm.service.TrainerService;
import com.epam.gymcrm.service.TrainingService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GymFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    public GymFacade(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    public List<Trainer> getTrainers() {
        return trainerService.getAll();
    }

    public List<Trainee> getTrainees() {
        return traineeService.getAll();
    }

    public List<Training> getTrainings() {
        return trainingService.getAll();
    }


    public Trainee createTrainee(Trainee trainee) {
        return traineeService.create(trainee);
    }

    public Trainer createTrainer(Trainer trainer) {
        return trainerService.create(trainer);
    }

    public Training createTraining(Training training) {
        return trainingService.create(training);
    }

    public void deleteTrainee(Long id) {
        traineeService.delete(id);
    }

    public Trainee updateTrainee(Long id, Trainee trainee) {
        return traineeService.update(id, trainee);
    }

    public Trainer updateTrainer(Long id, Trainer trainer) {
        return trainerService.update(id, trainer);
    }

    public Trainee getTraineeById(Long id) {
        return traineeService.getById(id);
    }

    public Trainer getTrainerById(Long id) {
        return trainerService.getById(id);
    }

}
