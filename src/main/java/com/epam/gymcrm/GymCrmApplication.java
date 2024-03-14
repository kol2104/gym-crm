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

		// Fetch all trainers
		List<Trainer> trainers = gymFacade.getTrainers();
		log.info("Trainers: {}", trainers);

		// Fetch all trainees
		List<Trainee> trainees = gymFacade.getTrainees();
		log.info("Trainees: {}", trainees);

		// Fetch all trainings
		List<Training> trainings = gymFacade.getTrainings();
		log.info("Trainings: {}", trainings);

		// Created a new trainee
		Trainee newTrainee = new Trainee();
		newTrainee.setFirstName("John");
		newTrainee.setLastName("Doe");
		newTrainee.setDateOfBirth(LocalDate.of(1999, Month.JANUARY, 20));
		newTrainee.setAddress("Address 1");
		newTrainee.setActive(true);
		Trainee createdTrainee = gymFacade.createTrainee(newTrainee);
		log.info("Created Trainee: {}", createdTrainee);

		// Created a new trainer
		Trainer newTrainer = new Trainer();
		newTrainer.setFirstName("Jane");
		newTrainer.setLastName("Smith");
		TrainingType trainingType = new TrainingType();
		trainingType.setName("running");
		newTrainer.setSpecialization(List.of(trainingType));
		Trainer createdTrainer = gymFacade.createTrainer(newTrainer);
		log.info("Created Trainer: {}", createdTrainer);

		// Created a new training
		Training newTraining = new Training();
		newTraining.setTrainingName("Gym Session");
		newTraining.setTrainingDate(
				LocalDateTime.of(2024, Month.JANUARY, 29, 17, 30, 0)
		);
		newTraining.setTrainingDuration(90);
		newTraining.setTrainingType(trainingType);
		newTraining.setTrainerId(createdTrainer.getId());
		newTraining.setTraineeId(createdTrainee.getId());
		Training createdTraining = gymFacade.createTraining(newTraining);
		log.info("Created Training: {}", createdTraining);

		// Update a trainee
		Trainee updatedTrainee = gymFacade.updateTrainee(createdTrainee.getId(), new Trainee());
		log.info("Updated Trainee: {}", updatedTrainee);

		// Update a trainer
		Trainer updatedTrainer = gymFacade.updateTrainer(createdTrainer.getId(), new Trainer());
		log.info("Updated Trainer: {}", updatedTrainer);

		// Find a trainee by ID
		Trainee foundTrainee = gymFacade.getTraineeById(createdTrainee.getId());
		log.info("Found Trainee by ID: {}", foundTrainee);

		// Find a trainer by ID
		Trainer foundTrainer = gymFacade.getTrainerById(createdTrainer.getId());
		log.info("Found Trainer by ID: {}", foundTrainer);

		// Delete a trainee
		gymFacade.deleteTrainee(createdTrainee.getId());
		log.info("Deleted Trainee with ID: {}", createdTrainee.getId());

		// Close the application context
		context.close();
	}

}
