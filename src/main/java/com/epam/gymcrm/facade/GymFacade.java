package com.epam.gymcrm.facade;

import com.epam.gymcrm.auth.annotation.Authenticated;
import com.epam.gymcrm.auth.Authentication;
import com.epam.gymcrm.model.Role;
import com.epam.gymcrm.model.Trainee;
import com.epam.gymcrm.model.Trainer;
import com.epam.gymcrm.model.Training;
import com.epam.gymcrm.model.TrainingCriteria;
import com.epam.gymcrm.service.TraineeService;
import com.epam.gymcrm.service.TrainerService;
import com.epam.gymcrm.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GymFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    private final Authentication authentication;

    public boolean authenticateTrainee(String username, String password) {
        if (traineeService.isUsernameAndPasswordValid(username, password)) {
            authentication.setRole(Role.TRAINEE);
            return true;
        }
        return false;
    }

    public boolean authenticateTrainer(String username, String password) {
        if (trainerService.isUsernameAndPasswordValid(username, password)) {
            authentication.setRole(Role.TRAINER);
            return true;
        }
        return false;
    }

    public Trainer createTrainerProfile(Trainer trainer) {
        return trainerService.create(trainer);
    }

    public Trainee createTraineeProfile(Trainee trainee) {
        return traineeService.create(trainee);
    }

    @Authenticated(roles = {Role.TRAINEE, Role.TRAINER})
    public Trainer getTrainerByUsername(String username) {
        return trainerService.getByUsername(username);
    }

    @Authenticated(roles = {Role.TRAINEE, Role.TRAINER})
    public Trainee getTraineeByUsername(String username) {
        return traineeService.getByUsername(username);
    }

    @Authenticated(roles = {Role.TRAINEE})
    public void changeTraineePassword(Long id, String newPassword) {
        traineeService.updatePassword(id, newPassword);
    }

    @Authenticated(roles = {Role.TRAINER})
    public void changeTrainerPassword(Long id, String newPassword) {
        trainerService.updatePassword(id, newPassword);
    }

    @Authenticated(roles = {Role.TRAINER})
    public Trainer updateTrainerProfile(Long id, Trainer updatedTrainer) {
        return trainerService.update(id, updatedTrainer);
    }

    @Authenticated(roles = {Role.TRAINEE})
    public Trainee updateTraineeProfile(Long id, Trainee updatedTrainee) {
        return traineeService.update(id, updatedTrainee);
    }

    @Authenticated(roles = {Role.TRAINEE})
    public void activateTrainee(Long id) {
        traineeService.activate(id);
    }

    @Authenticated(roles = {Role.TRAINEE})
    public void deactivateTrainee(Long id) {
        traineeService.deactivate(id);
    }


    @Authenticated(roles = {Role.TRAINER})
    public void activateTrainer(Long id) {
        trainerService.activate(id);
    }

    @Authenticated(roles = {Role.TRAINER})
    public void deactivateTrainer(Long id) {
        trainerService.deactivate(id);
    }

    @Authenticated(roles = {Role.TRAINEE})
    public void deleteTraineeProfile(String username) {
        traineeService.deleteByUsername(username);
    }

    @Authenticated(roles = {Role.TRAINEE, Role.TRAINER})
    public List<Training> getTraineeTrainings(String traineeUsername, Map<TrainingCriteria, Object> criteria) {
        return trainingService.getTrainingByTraineeUsernameAndCriteria(traineeUsername, criteria);
    }

    @Authenticated(roles = {Role.TRAINEE, Role.TRAINER})
    public List<Training> getTrainerTrainings(String trainerUsername, Map<TrainingCriteria, Object> criteria) {
        return trainingService.getTrainingByTrainerUsernameAndCriteria(trainerUsername, criteria);
    }

    @Authenticated(roles = {Role.TRAINEE, Role.TRAINER})
    public Training addTraining(Training training) {
        return trainingService.create(training);
    }

    @Authenticated(roles = {Role.TRAINEE, Role.TRAINER})
    public List<Trainer> getUnassignedTrainers(String traineeUsername) {
        return traineeService.getUnassignedOnTraineeTrainerListByUsername(traineeUsername);
    }

    @Authenticated(roles = {Role.TRAINEE, Role.TRAINER})
    public void updateTraineeTrainersList(Long id, List<Trainer> trainers) {
        traineeService.updateTrainersList(id, trainers);
    }
}
