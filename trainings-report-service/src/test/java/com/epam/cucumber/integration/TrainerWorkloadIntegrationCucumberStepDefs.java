package com.epam.cucumber.integration;

import com.epam.TrainingsReportApplication;
import com.epam.dto.TrainerWorkloadRequestDto;
import com.epam.message.TrainerWorkloadJmsConsumer;
import com.epam.model.Month;
import com.epam.model.TrainerWorkload;
import com.epam.model.Year;
import com.epam.repository.impl.TrainerWorkloadMongoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import jakarta.jms.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@CucumberContextConfiguration
@SpringBootTest(classes = TrainingsReportApplication.class)
@ActiveProfiles("test")
public class TrainerWorkloadIntegrationCucumberStepDefs {

    private Integer MAX_TIMEOUT = 5000;

    @Autowired
    private TrainerWorkloadMongoRepository trainerWorkloadRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private TrainerWorkloadJmsConsumer trainerWorkloadJmsConsumer;
    @Autowired
    private ConnectionFactory connectionFactory;
    @Autowired
    private ObjectMapper objectMapper;

    @Value("${spring.activemq.topic-name}")
    private String topic;

    private JmsTemplate jmsTemplate;

    private TrainerWorkload trainerWorkload;
    private TrainerWorkload trainerWorkloadFromDatabase;
    private TrainerWorkloadRequestDto trainerWorkloadRequestDto;

    @Given("a trainer workload named {string}")
    public void aTrainerWorkloadNamed(String username) {
        List<Month> months = new ArrayList<>();
        months.add(new com.epam.model.Month("MAY", 100L));
        List<Year> years = new ArrayList<>();
        years.add(new Year(2022, months));
        trainerWorkload = new TrainerWorkload(username, "firstname", "lastname", true, years);
    }

    @And("a empty workload for {string}")
    public void aEmptyWorkloadFor(String username) {
        assertNull(mongoTemplate.findById(username, TrainerWorkload.class));
    }

    @When("I save workload")
    public void iSendRequestForSaveWorkload() {
        trainerWorkloadRepository.persistTrainerWorkload(trainerWorkload);
    }

    @Then("saved workload for {string}")
    public void savedWorkloadFor(String username) {
        assertEquals(trainerWorkload, mongoTemplate.findById(username, TrainerWorkload.class));
    }

    @And("exist workload for {string}")
    public void existWorkloadFor(String username) {
        aTrainerWorkloadNamed(username);
        mongoTemplate.save(trainerWorkload);
    }

    @When("I find the workload for {string}")
    public void iFindTheWorkloadFor(String username) {
        trainerWorkloadFromDatabase = mongoTemplate.findById(username, TrainerWorkload.class);
    }

    @Then("trainer workload found")
    public void trainerWorkloadFound() {
        assertEquals(trainerWorkload, trainerWorkloadFromDatabase);
    }

    @Given("message in topic for {string}")
    public void messageInTopicFor(String username) {
        aTrainerWorkloadNamed(username);
        trainerWorkloadRequestDto = new TrainerWorkloadRequestDto(
            username, trainerWorkload.getFirstname(),
            trainerWorkload.getLastname(), trainerWorkload.getIsActive(),
            LocalDateTime.of(2024, java.time.Month.JANUARY, 1, 1, 0, 0),
            120L, true
        );
        jmsTemplateTopic(objectMapper);
        jmsTemplate.convertAndSend(topic, trainerWorkloadRequestDto);
    }

    @When("wait for message")
    public void waitForMessage() throws InterruptedException {
        Thread.sleep(MAX_TIMEOUT);
    }

    @Then("message is received for {string}")
    public void messageIsReceived(String username) {
        assertNotNull(mongoTemplate.findById(username, TrainerWorkload.class));
    }

    private MessageConverter messageConverter(ObjectMapper objectMapper) {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        converter.setObjectMapper(objectMapper);
        return converter;
    }

    private void jmsTemplateTopic(ObjectMapper objectMapper) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.setMessageConverter(messageConverter(objectMapper));
        this.jmsTemplate = jmsTemplate;
    }
}
