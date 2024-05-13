package com.epam.cucumber.component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.epam.TrainingsReportApplication;
import com.epam.dto.TrainerWorkloadRequestDto;
import com.epam.dto.TrainerWorkloadResponseDto;
import com.epam.model.TrainerWorkload;
import com.epam.model.Year;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@CucumberContextConfiguration
@AutoConfigureMockMvc
@SpringBootTest(classes = TrainingsReportApplication.class)
@EnableAutoConfiguration(exclude= {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@ActiveProfiles("test")
public class TrainerWorkloadComponentCucumberStepDefs {

    @MockBean
    private MongoTemplate mongoTemplate;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Value("${jwt.key}")
    private String secret;

    @Value("${jwt.exp}")
    private long exp;

    private TrainerWorkloadRequestDto requestDto;
    private TrainerWorkload trainerWorkload;
    private String token;
    private MvcResult mvcResult;

    @Given("a trainer named {string}")
    public void a_trainer_named_and_a_workload_update(String username) {
        requestDto = new TrainerWorkloadRequestDto(username, "firstname", "lastname",
            true, LocalDateTime.of(2024, Month.JANUARY, 1, 1, 0, 0),
            120L, true);
    }

    @And("a mocked trainer workload for {string} from DB")
    public void mockedTrainerWorkloadFromDB(String username) {
        List<com.epam.model.Month> months = new ArrayList<>();
        months.add(new com.epam.model.Month("MAY", 100L));
        List<Year> years = new ArrayList<>();
        years.add(new Year(2022, months));
        trainerWorkload = new TrainerWorkload(username, "firstname", "lastname", true, years);
        when(mongoTemplate.findById(username, TrainerWorkload.class)).thenReturn(trainerWorkload);
    }

    @And("a authentication token for {string}")
    public void authenticationToken(String username) {
        token = "Bearer " + JWT.create()
            .withClaim("sub", username)
            .withExpiresAt(Instant.now().plusSeconds(exp))
            .sign(Algorithm.HMAC256(secret));
    }

    @When("I send request for update workload")
    public void i_submit_the_update_for() throws Exception {
        mvcResult = mockMvc.perform(post("/api/trainer-workload")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .header("Authorization", token))
            .andReturn();
    }

    @Then("the workload for {string} should be updated successfully")
    public void the_workload_should_be_updated_successfully(String username) {
        verify(mongoTemplate, times(1)).save(
            new TrainerWorkload(username, "firstname", "lastname", true, List.of(
                new Year(2022, List.of(new com.epam.model.Month(Month.MAY.name(), 100L))),
                new Year(2024, List.of(new com.epam.model.Month(Month.JANUARY.name(), 120L)))
            ))
        );
    }

    @When("I retrieve the workload for {string}")
    public void i_retrieve_the_workload_for(String username) throws Exception {
        mvcResult = mockMvc.perform(get("/api/trainer-workload/" + username)
                .header("Authorization", token))
            .andReturn();
    }

    @Then("response status is {string}")
    public void responseStatusIs(String status) {
        assertEquals(status, String.valueOf(mvcResult.getResponse().getStatus()));
    }

    @And("workload for {string} was called")
    public void workloadForWasCalled(String username) {
        verify(mongoTemplate, times(1)).findById(username, TrainerWorkload.class);
    }

    @And("returned workload details")
    public void returnedWorkloadDetails() throws UnsupportedEncodingException, JsonProcessingException {
        TrainerWorkloadResponseDto returnedWorkload = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(), TrainerWorkloadResponseDto.class);
        assertNotNull(returnedWorkload);
        assertEquals(trainerWorkload.getUsername(), returnedWorkload.username());
        assertEquals(trainerWorkload.getFirstname(), returnedWorkload.firstname());
        assertEquals(trainerWorkload.getLastname(), returnedWorkload.lastname());
        assertEquals(trainerWorkload.getIsActive(), returnedWorkload.isActive());
        assertEquals(trainerWorkload.getYears().size(), returnedWorkload.years().size());
    }

    @And("a mocked empty trainer workload from DB")
    public void aMockedEmptyTrainerWorkloadFromDB() {
        when(mongoTemplate.findById(any(), eq(TrainerWorkload.class))).thenReturn(null);
    }

    @And("the workload for {string} should be created successfully")
    public void theWorkloadForShouldBeCreatedSuccessfully(String username) {
        verify(mongoTemplate, times(1)).save(
            new TrainerWorkload(username, "firstname", "lastname", true, List.of(
                new Year(2024, List.of(new com.epam.model.Month(Month.JANUARY.name(), 120L)))
            ))
        );
    }
}
