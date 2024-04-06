package com.epam.gymcrm;

import com.epam.gymcrm.config.SpringConfiguration;
import com.epam.gymcrm.facade.GymFacade;
import com.epam.gymcrm.model.Trainee;
import com.epam.gymcrm.model.Trainer;
import com.epam.gymcrm.model.Training;
import com.epam.gymcrm.model.TrainingCriteria;
import com.epam.gymcrm.model.TrainingType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

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
		TrainingType trainingType = new TrainingType();
		trainingType.setId(2L);
		trainingType.setName("running");

		Trainee createdTrainee = createTrainee(gymFacade, "John", "Doe",
				LocalDate.of(1996, Month.JANUARY, 21), "Address 1", true);
		Trainer createdTrainer = createTrainer(gymFacade, "Jane", "Smith", true, trainingType);

		traineeOperations(gymFacade, createdTrainee);
		trainerOperations(gymFacade, createdTrainer);

		createTraining(gymFacade, createdTrainee, createdTrainer, trainingType);

		Trainee updatedTrainee = updateTrainee(gymFacade, createdTrainee);
		Trainer updatedTrainer = updateTrainer(gymFacade, createdTrainer);

		traineesAndTrainersOperations(gymFacade, updatedTrainee);
		getTrainingsByCriteria(gymFacade, updatedTrainee, updatedTrainer);

		deleteTrainee(gymFacade, updatedTrainee.getUsername(), updatedTrainee.getPassword());
	}

	private static void getTrainingsByCriteria(GymFacade gymFacade, Trainee updatedTrainee, Trainer updatedTrainer) {
		List<Training> trainings = gymFacade.getTraineeTrainings(updatedTrainee.getUsername(), null);
		log.info("Training by trainee username: {}", trainings);
		trainings = gymFacade.getTrainerTrainings(updatedTrainer.getUsername(), null);
		log.info("Training by trainer username: {}", trainings);

		Map<TrainingCriteria, Object> criteria = new EnumMap<>(TrainingCriteria.class);
		criteria.put(TrainingCriteria.FROM_DATE, LocalDateTime.of(2025, Month.JANUARY, 21, 17, 30, 0));
		trainings = gymFacade.getTrainerTrainings(updatedTrainer.getUsername(), criteria);
		log.info("Training by trainer username and criteria '{}': {}", criteria, trainings);

		criteria = new EnumMap<>(TrainingCriteria.class);
		criteria.put(TrainingCriteria.TO_DATE, LocalDateTime.of(2025, Month.JANUARY, 21, 17, 30, 0));
		trainings = gymFacade.getTrainerTrainings(updatedTrainer.getUsername(), criteria);
		log.info("Training by trainer username and criteria '{}': {}", criteria, trainings);

		criteria = new EnumMap<>(TrainingCriteria.class);
		TrainingType trainingType = new TrainingType();
		trainingType.setId(2L);
		trainingType.setName("running");
		criteria.put(TrainingCriteria.TRAINING_TYPE, trainingType);
		trainings = gymFacade.getTrainerTrainings(updatedTrainer.getUsername(), criteria);
		log.info("Training by trainer username and criteria '{}': {}", criteria, trainings);
	}

	private static void traineeOperations(GymFacade gymFacade, Trainee createdTrainee) {
		// Trainee username and password matching
		boolean isAuthenticationAsTraineeSuccess = gymFacade.authenticateTrainee(createdTrainee.getUsername(), createdTrainee.getPassword());
		log.info("Authenticated as trainee: {}", isAuthenticationAsTraineeSuccess);

		// Activate/De-activate trainee
		gymFacade.deactivateTrainee(createdTrainee.getId());
		gymFacade.activateTrainee(createdTrainee.getId());

		// Select Trainee profile by username
		Trainee selectedTrainee = gymFacade.getTraineeByUsername(createdTrainee.getUsername());
		log.info("Found trainee: {}", selectedTrainee);

		gymFacade.changeTraineePassword(createdTrainee.getId(), "newPassword");
	}

	private static void trainerOperations(GymFacade gymFacade, Trainer createdTrainer) {
		// Trainer username and password matching
		boolean isAuthenticationAsTrainerSuccess = gymFacade.authenticateTrainer(createdTrainer.getUsername(), createdTrainer.getPassword());
		log.info("Authenticated as trainer: {}", isAuthenticationAsTrainerSuccess);

		// Activate/De-activate trainer
		gymFacade.activateTrainer(createdTrainer.getId());
		gymFacade.deactivateTrainer(createdTrainer.getId());

		// Select Trainer profile by username
		Trainer selectedTrainer = gymFacade.getTrainerByUsername(createdTrainer.getUsername());
		log.info("Found trainer: {}", selectedTrainer);

		// Trainer password change
		gymFacade.changeTrainerPassword(createdTrainer.getId(), "newPassword");
	}

	private static Trainee createTrainee(GymFacade gymFacade, String firstName, String lastName,
										 LocalDate dateOfBirth, String address, Boolean isActive) {
		// Created a new trainee
		Trainee newTrainee = buildTrainee(firstName, lastName, dateOfBirth, address, isActive);
		Trainee createdTrainee = gymFacade.createTraineeProfile(newTrainee);
		log.info("Created Trainee: {}", createdTrainee);
		return createdTrainee;
	}

	private static Trainee buildTrainee(String firstName, String lastName,
										LocalDate dateOfBirth, String address, Boolean isActive) {
		Trainee newTrainee = new Trainee();
		newTrainee.setFirstName(firstName);
		newTrainee.setLastName(lastName);
		newTrainee.setDateOfBirth(dateOfBirth);
		newTrainee.setAddress(address);
		newTrainee.setActive(isActive);
		return newTrainee;
	}

	private static Trainer createTrainer(GymFacade gymFacade, String firstName, String lastName,
										 Boolean isActive, TrainingType trainingType) {
		// Created a new trainer
		Trainer newTrainer = buildTrainer(firstName, lastName, isActive, trainingType);
		Trainer createdTrainer = gymFacade.createTrainerProfile(newTrainer);
		log.info("Created Trainer: {}", createdTrainer);
		return createdTrainer;
	}

	private static Trainer buildTrainer(String firstName, String lastName,
										Boolean isActive, TrainingType trainingType) {
		Trainer newTrainer = new Trainer();
		newTrainer.setFirstName(firstName);
		newTrainer.setLastName(lastName);
		newTrainer.setActive(isActive);
		newTrainer.setSpecialization(trainingType);
		return newTrainer;
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
		newTraining.setTrainer(trainer);
		newTraining.setTrainee(trainee);
		Training createdTraining = gymFacade.addTraining(newTraining);
		log.info("Created Training: {}", createdTraining);
		return createdTraining;
	}

	private static void deleteTrainee(GymFacade gymFacade, String username, String password) {
		// Trainee username and password matching
		boolean isAuthenticationAsTraineeSuccess = gymFacade.authenticateTrainee(username, password);
		log.info("Authenticated as trainee: {}", isAuthenticationAsTraineeSuccess);
		// Delete trainee profile by username
		gymFacade.deleteTraineeProfile(username);
	}

	private static Trainer updateTrainer(GymFacade gymFacade, Trainer createdTrainer) {
		// Trainer username and password matching
		boolean isAuthenticationAsTrainerSuccess = gymFacade.authenticateTrainer(createdTrainer.getUsername(), "newPassword");
		log.info("Authenticated as trainer: {}", isAuthenticationAsTrainerSuccess);

		TrainingType newTrainingType = new TrainingType();
		newTrainingType.setId(1L);
		newTrainingType.setName("fitness");

		Trainer newTrainer = buildTrainer("trainer", "lastName", true, newTrainingType);
		Trainer updatedTrainer = gymFacade.updateTrainerProfile(createdTrainer.getId(), newTrainer);
		log.info("Updated trainer: {}", updatedTrainer);
		return updatedTrainer;
	}

	private static Trainee updateTrainee(GymFacade gymFacade, Trainee createdTrainee) {
		// Trainer username and password matching
		boolean isAuthenticationAsTraineeSuccess = gymFacade.authenticateTrainee(createdTrainee.getUsername(), "newPassword");
		log.info("Authenticated as trainee: {}", isAuthenticationAsTraineeSuccess);

		Trainee newTrainee = buildTrainee("trainee", "lastName",
				LocalDate.of(1995, Month.APRIL, 10), "Address 2", true);
		Trainee updatedTrainee = gymFacade.updateTraineeProfile(createdTrainee.getId(), newTrainee);
		log.info("Updated trainee: {}", updatedTrainee);
		return updatedTrainee;
	}

	private static void traineesAndTrainersOperations(GymFacade gymFacade, Trainee trainee) {
		List<Trainer> trainers = gymFacade.getUnassignedTrainers(trainee.getUsername());
		log.info("Unassigned trainers to trainee with username '{}': {}", trainee.getUsername(), trainers);

		gymFacade.updateTraineeTrainersList(trainee.getId(), trainers);

		trainers = gymFacade.getUnassignedTrainers(trainee.getUsername());
		log.info("Unassigned trainers to trainee with username '{}' after update: {}", trainee.getUsername(), trainers);
	}
}
