package com.epam.gymcrm;

import com.epam.gymcrm.config.SpringConfiguration;
import com.epam.gymcrm.facade.GymFacade;
import com.epam.gymcrm.model.Trainee;
import com.epam.gymcrm.model.Trainer;
import com.epam.gymcrm.model.Training;
import com.epam.gymcrm.model.TrainingType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@Slf4j
public class GymCrmApplication {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);

		GymFacade gymFacade = context.getBean(GymFacade.class);

		modelOperations(gymFacade);

		// Close the application context
		context.close();
	}

	private static void modelOperations(GymFacade gymFacade) {
		printAllModels(gymFacade);

		TrainingType trainingType = new TrainingType();
		trainingType.setName("running");

		Trainee createdTrainee = createTrainee(gymFacade);
		Trainer createdTrainer = createTrainer(gymFacade, trainingType);
		createTraining(gymFacade, createdTrainee, createdTrainer, trainingType);

		updateTrainee(gymFacade, createdTrainee.getId());

		updateTrainer(gymFacade, createdTrainer.getId());

		printTraineeById(gymFacade, createdTrainee.getId());

		printTrainerById(gymFacade, createdTrainer.getId());

		deleteTrainee(gymFacade, createdTrainee.getId());
	}

	private static void printAllModels(GymFacade gymFacade) {
		// Fetch all trainers
		List<Trainer> trainers = gymFacade.getTrainers();
		log.info("Trainers: {}", trainers);

		// Fetch all trainees
		List<Trainee> trainees = gymFacade.getTrainees();
		log.info("Trainees: {}", trainees);

		// Fetch all trainings
		List<Training> trainings = gymFacade.getTrainings();
		log.info("Trainings: {}", trainings);
	}

	private static void updateTrainee(GymFacade gymFacade, Long traineeId) {
		Trainee updatedTrainee = gymFacade.updateTrainee(traineeId, new Trainee());
		log.info("Updated Trainee: {}", updatedTrainee);
	}

	private static void updateTrainer(GymFacade gymFacade, Long trainerId) {
		Trainer updatedTrainer = gymFacade.updateTrainer(trainerId, new Trainer());
		log.info("Updated Trainer: {}", updatedTrainer);
	}

	private static void deleteTrainee(GymFacade gymFacade, Long traineeId) {
		gymFacade.deleteTrainee(traineeId);
		log.info("Deleted Trainee with ID: {}", traineeId);
	}

	private static Trainee createTrainee(GymFacade gymFacade) {
		// Created a new trainee
		Trainee newTrainee = new Trainee();
		newTrainee.setFirstName("John");
		newTrainee.setLastName("Doe");
		newTrainee.setDateOfBirth(LocalDate.of(1999, Month.JANUARY, 20));
		newTrainee.setAddress("Address 1");
		newTrainee.setActive(true);
		Trainee createdTrainee = gymFacade.createTrainee(newTrainee);
		log.info("Created Trainee: {}", createdTrainee);
		return createdTrainee;
	}

	private static Trainer createTrainer(GymFacade gymFacade, TrainingType trainingType) {
		// Created a new trainer
		Trainer newTrainer = new Trainer();
		newTrainer.setFirstName("Jane");
		newTrainer.setLastName("Smith");
		newTrainer.setSpecialization(List.of(trainingType));
		Trainer createdTrainer = gymFacade.createTrainer(newTrainer);
		log.info("Created Trainer: {}", createdTrainer);
		return createdTrainer;
	}

	private static Training createTraining(GymFacade gymFacade, Trainee trainee,
										   Trainer trainer, TrainingType trainingType) {
		// Created a new training
		Training newTraining = new Training();
		newTraining.setTrainingName("Gym Session");
		newTraining.setTrainingDate(
				LocalDateTime.of(2024, Month.JANUARY, 29, 17, 30, 0)
		);
		newTraining.setTrainingDuration(90);
		newTraining.setTrainingType(trainingType);
		newTraining.setTrainerId(trainee.getId());
		newTraining.setTraineeId(trainer.getId());
		Training createdTraining = gymFacade.createTraining(newTraining);
		log.info("Created Training: {}", createdTraining);
		return createdTraining;
	}

	private static void printTraineeById(GymFacade gymFacade, Long traineeId) {
		Trainee foundTrainee = gymFacade.getTraineeById(traineeId);
		log.info("Found Trainee by ID: {}", foundTrainee);
	}

	private static void printTrainerById(GymFacade gymFacade, Long trainerId) {
		Trainer foundTrainer = gymFacade.getTrainerById(trainerId);
		log.info("Found Trainee by ID: {}", foundTrainer);
	}

}
