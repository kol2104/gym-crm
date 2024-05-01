package com.epam.controller;

import com.epam.controller.handler.TrainingReportExceptionHandler;
import com.epam.dto.TrainerWorkloadRequestDto;
import com.epam.service.TrainerWorkloadService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TrainerWorkloadControllerTest {

    private AutoCloseable mocksClose;
    private MockMvc mockMvc;
    @Mock
    private TrainerWorkloadService trainerWorkloadService;
    @InjectMocks
    private TrainerWorkloadController trainerWorkloadController;

    @BeforeEach
    public void setUp() {
        mocksClose = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(trainerWorkloadController, new TrainingReportExceptionHandler()).build();
    }

    @AfterEach
    public void close() throws Exception {
        mocksClose.close();
    }

    @Test
    void testUpdateTrainerWorkload() throws Exception {
        mockMvc.perform(post("/api/trainer-workload")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{" +
                        "\"username\": \"name\"," +
                        "\"firstname\": \"name\"," +
                        "\"lastname\": \"name\"," +
                        "\"isActive\": \"true\"," +
                        "\"trainingDate\": \"2024-01-01 00:00:00\"," +
                        "\"trainingDuration\": \"90\"," +
                        "\"action\": \"true\"" +
                    "}"
                ))
            .andExpect(status().isOk());

        verify(trainerWorkloadService).updateTrainerWorkload(any(TrainerWorkloadRequestDto.class));
    }

    @Test
    void testGetTrainerWorkload() throws Exception {
        mockMvc.perform(get("/api/trainer-workload/{username}", "name"))
            .andExpect(status().isOk());

        verify(trainerWorkloadService).getTrainerWorkload(any(String.class));
    }
}
