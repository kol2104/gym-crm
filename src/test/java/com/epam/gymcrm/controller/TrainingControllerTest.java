package com.epam.gymcrm.controller;

import com.epam.gymcrm.controller.handler.GymCrmExceptionHandler;
import com.epam.gymcrm.dto.training.TrainingDto;
import com.epam.gymcrm.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TrainingControllerTest {

    private MockMvc mockMvc;
    @Mock
    private TrainingService trainingService;
    @InjectMocks
    private TrainingController trainingController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(trainingController, new GymCrmExceptionHandler()).build();
    }

    @Test
    void testGetTraineeTrainings() throws Exception {
        String username = "testuser";
        List<TrainingDto> trainings = List.of(
                new TrainingDto(null, null, null, null, null, null),
                new TrainingDto(null, null, null, null, null, null)
        );
        when(trainingService.getTrainingByTraineeUsernameAndCriteria(username, null)).thenReturn(trainings);

        mockMvc.perform(get("/api/trainings/trainee/{username}", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(trainings.size()));

        verify(trainingService).getTrainingByTraineeUsernameAndCriteria(username, null);
    }

    @Test
    void testGetTrainerTrainings() throws Exception {
        String username = "testuser";
        List<TrainingDto> trainings = List.of(
                new TrainingDto(null, null, null, null, null, null),
                new TrainingDto(null, null, null, null, null, null)
        );
        when(trainingService.getTrainingByTrainerUsernameAndCriteria(username, null)).thenReturn(trainings);

        mockMvc.perform(get("/api/trainings/trainer/{username}", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(trainings.size()));

        verify(trainingService).getTrainingByTrainerUsernameAndCriteria(username, null);
    }

    @Test
    void testCreateTrainee() throws Exception {
        mockMvc.perform(post("/api/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                "{" +
                                        "\"trainingName\": \"name\"," +
                                        "\"trainingDate\": \"2024-01-01 00:00:00\"," +
                                        "\"trainingTypeName\": \"running\"," +
                                        "\"trainingDuration\": \"90\"," +
                                        "\"traineeUsername\": \"name\"," +
                                        "\"trainerUsername\": \"name\"" +
                                "}"
                        ))
                .andExpect(status().isOk());

        verify(trainingService).create(any(TrainingDto.class));
    }
}
