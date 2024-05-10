package com.epam.gymcrm.cucumber.integration;

import com.epam.gymcrm.GymCrmApplication;
import com.epam.gymcrm.dao.impl.TraineeDaoDatabase;
import com.epam.gymcrm.dao.impl.TrainerDaoDatabase;
import com.epam.gymcrm.dto.TrainerWorkloadRequestDto;
import com.epam.gymcrm.message.TrainingsReportService;
import com.epam.gymcrm.model.Trainee;
import com.epam.gymcrm.model.Trainer;
import com.epam.gymcrm.model.TrainingType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@CucumberContextConfiguration
@SpringBootTest(classes = GymCrmApplication.class)
@ActiveProfiles("test")
public class GymServiceIntegrationCucumberStepDefs {

    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    private TraineeDaoDatabase traineeDaoDatabase;
    @Autowired
    private TrainerDaoDatabase trainerDaoDatabase;
    @Autowired
    private TrainingsReportService trainingsReportService;
    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${spring.activemq.topic-name}")
    private String topic;

    private EntityManager entityManager;
    private Trainee trainee;
    private Trainer trainer;
    private Optional<Trainer> trainerFromDB;
    private TrainerWorkloadRequestDto trainerWorkloadRequestDto;

    @Given("a trainee named {string}")
    public void aTraineeNamed(String username) {
        trainee = new Trainee();
        trainee.setId(1L);
        trainee.setUsername(username);
        trainee.setFirstName("firstname");
        trainee.setLastName("firstname");
        trainee.setPassword("password");
        trainee.setActive(true);
        trainee.setDateOfBirth(LocalDate.MIN);
        trainee.setAddress("Address 1");
        trainee.setTrainers(new ArrayList<>());
    }

    @And("a not existing trainee named {string} in DB")
    public void aEmptyTraineeFor(String username) {
        entityManager = entityManagerFactory.createEntityManager();
        Optional<Trainee> foundTrainee = entityManager
            .createQuery("from Trainee t left join fetch t.trainers where t.username = :username", Trainee.class)
            .setParameter("username", username)
            .getResultStream().findFirst();
        assertFalse(foundTrainee.isPresent());
        entityManager.close();
    }

    @When("I save trainee")
    public void iSaveTrainee() {
        traineeDaoDatabase.create(trainee);
    }

    @Then("saved trainee named {string}")
    public void savedTraineeNamed(String username) {
        entityManager = entityManagerFactory.createEntityManager();
        Optional<Trainee> foundTrainee = entityManager
            .createQuery("from Trainee t left join fetch t.trainers where t.username = :username", Trainee.class)
            .setParameter("username", username)
            .getResultStream().findFirst();
        assertTrue(foundTrainee.isPresent());
        entityManager.close();
    }

    @Given("exist trainer named {string}")
    public void existTrainerNamed(String username) {
        entityManager = entityManagerFactory.createEntityManager();
        trainer = new Trainer(1L);
        trainer.setUsername(username);
        trainer.setFirstName("firstname");
        trainer.setLastName("firstname");
        trainer.setPassword("password");
        trainer.setActive(true);
        TrainingType trainingType = new TrainingType();
        trainingType.setId(100L);
        trainingType.setName("name");
        entityManager.persist(trainingType);
        trainer.setSpecialization(trainingType);
        trainer.setTrainees(new ArrayList<>());
        entityManager.persist(trainer);
        entityManager.close();
    }

    @When("I find the trainer named {string}")
    public void iFindTheTrainerNamed(String username) {
        trainerFromDB = trainerDaoDatabase.getByUsername(username);
    }

    @Then("trainer found")
    public void trainerFound() {
        assertTrue(trainerFromDB.isPresent());
        assertEquals(trainer, trainerFromDB.get());
    }

    @Given("message for topic with trainer {string}")
    public void messageForTopicWithTrainer(String username) {
        trainerWorkloadRequestDto = new TrainerWorkloadRequestDto(username, "firstname",
            "lastname", true, LocalDateTime.MIN, 120, true);
    }

    @When("message sending to topic")
    public void messageSendingToTopic() {
        trainingsReportService.updateTrainerWorkload(trainerWorkloadRequestDto);
    }

    @Then("message in topic")
    public void messageInTopic() {
        TrainerWorkloadRequestDto workloadFromTopic = (TrainerWorkloadRequestDto) jmsTemplate.receiveAndConvert();
        assertEquals(trainerWorkloadRequestDto, workloadFromTopic);
    }
}
