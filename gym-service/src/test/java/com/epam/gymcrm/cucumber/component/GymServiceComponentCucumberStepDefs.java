package com.epam.gymcrm.cucumber.component;

import com.epam.gymcrm.GymCrmApplication;
import com.epam.gymcrm.dao.TraineeDao;
import com.epam.gymcrm.dao.TrainerDao;
import com.epam.gymcrm.dao.TrainingDao;
import com.epam.gymcrm.dao.TrainingTypeDao;
import com.epam.gymcrm.dao.UserDao;
import com.epam.gymcrm.dto.TrainerWorkloadRequestDto;
import com.epam.gymcrm.dto.training.TrainingDto;
import com.epam.gymcrm.message.TrainingsReportService;
import com.epam.gymcrm.model.Trainee;
import com.epam.gymcrm.model.Trainer;
import com.epam.gymcrm.model.TrainingType;
import com.epam.gymcrm.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@CucumberContextConfiguration
@AutoConfigureMockMvc
@SpringBootTest(classes = GymCrmApplication.class)
@ActiveProfiles("test")
public class GymServiceComponentCucumberStepDefs {

    @MockBean
    private TrainingDao trainingDao;
    @MockBean
    private TraineeDao traineeDao;
    @MockBean
    private TrainerDao trainerDao;
    @MockBean
    private UserDao userDao;
    @MockBean
    private TrainingTypeDao trainingTypeDao;
    @MockBean
    private TrainingsReportService trainingsReportService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtUtil jwtUtil;

    private String token;
    private MvcResult mvcResult;
    private TrainingDto trainingDto;
    private List<TrainingType> trainingTypes;
    private Trainer trainer;

    @Given("a training to save with trainee {string}, trainer {string} and training type {string}")
    public void aTrainingToSaveWithTraineeAndTrainer(String traineeUsername, String trainerUsername,
                                                     String trainingTypeName) {
        trainingDto = new TrainingDto("TrainingName", LocalDateTime.MIN,
            trainingTypeName, 120L, traineeUsername, trainerUsername);
    }

    @And("a mocked trainer {string} from DB")
    public void aMockedTrainerFromDB(String username) {
        trainer = new Trainer();
        trainer.setUsername(username);
        when(trainerDao.getByUsername(username)).thenReturn(Optional.of(trainer));
    }

    @And("a mocked trainee {string} from DB")
    public void aMockedTraineeFromDB(String username) {
        Trainee trainee = new Trainee();
        trainee.setUsername(username);
        when(traineeDao.getByUsername(username)).thenReturn(Optional.of(trainee));
    }

    @And("a mocked training type {string} from DB")
    public void aMockedTrainingTypeFromDB(String trainingTypeName) {
        TrainingType trainingType = new TrainingType();
        trainingType.setName(trainingTypeName);
        when(trainingTypeDao.getByName(trainingTypeName)).thenReturn(Optional.of(trainingType));
    }

    @And("a authentication token for {string}")
    public void aAuthenticationTokenFor(String username) {
        token = jwtUtil.generateToken(username, List.of(() -> "ROLE_TRAINER"));
    }

    @When("I send request to save training")
    public void iSendRequestToSaveTraining() throws Exception {
        mvcResult = mockMvc.perform(post("/api/trainings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trainingDto))
                .header("Authorization", token))
            .andReturn();
    }

    @Then("response status is {string}")
    public void responseStatusIs(String status) {
        assertEquals(status, String.valueOf(mvcResult.getResponse().getStatus()));
    }

    @And("trainer {string} from DB was called")
    public void trainerFromDBWasCalled(String username) {
        verify(trainerDao, times(1)).getByUsername(username);
    }

    @And("trainee {string} from DB was called")
    public void traineeFromDBWasCalled(String username) {
        verify(traineeDao, times(1)).getByUsername(username);
    }

    @And("training type {string} from DB was called")
    public void trainingTypeFromDBWasCalled(String trainingTypeName) {
        verify(trainingTypeDao, times(1)).getByName(trainingTypeName);
    }

    @And("message for training report service was created with trainer {string}")
    public void messageForTrainingReportServiceWasCreated(String username) {
        TrainerWorkloadRequestDto trainerWorkloadRequestDto = new TrainerWorkloadRequestDto(username,
            trainer.getFirstName(), trainer.getLastName(), trainer.isActive(),
            trainingDto.trainingDate(), trainingDto.trainingDuration().intValue(), true);
        verify(trainingsReportService, times(1)).updateTrainerWorkload(trainerWorkloadRequestDto);
    }

    @When("I retrieve training types")
    public void iRetrieveTrainingTypes() throws Exception {
        mvcResult = mockMvc.perform(get("/api/training-types")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token))
            .andReturn();
    }

    @And("returned training types equal types in DB")
    public void returnedTrainingTypes() throws UnsupportedEncodingException, JsonProcessingException {
        List<TrainingType> trainingTypeListFromRequest = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(), new TypeReference<>() {}
        );
        assertEquals(trainingTypes.size(), trainingTypeListFromRequest.size());
        assertEquals(trainingTypes, trainingTypeListFromRequest);
    }

    @And("a mocked empty trainee {string} from DB")
    public void aMockedEmptyTraineeFromDB(String username) {
        when(traineeDao.getByUsername(username)).thenReturn(Optional.empty());
    }

    @And("mocked training types from DB")
    public void mockedTrainingTypesFromDB() {
        TrainingType trainingType1 = new TrainingType();
        trainingType1.setId(100L);
        trainingType1.setName("TrainingType1");
        TrainingType trainingType2 = new TrainingType();
        trainingType2.setId(101L);
        trainingType2.setName("TrainingType2");
        trainingTypes = List.of(trainingType1, trainingType2);
        when(trainingTypeDao.getAll()).thenReturn(trainingTypes);
    }
}
