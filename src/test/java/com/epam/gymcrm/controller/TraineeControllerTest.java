package com.epam.gymcrm.controller;

import com.epam.gymcrm.controller.handler.GymCrmExceptionHandler;
import com.epam.gymcrm.dto.trainee.TraineeForUpdateRequestDto;
import com.epam.gymcrm.dto.trainee.TraineeRequestDto;
import com.epam.gymcrm.dto.trainee.TraineeResponseDto;
import com.epam.gymcrm.dto.trainer.PlainTrainerResponseDto;
import com.epam.gymcrm.dto.trainer.TrainerUsernameDto;
import com.epam.gymcrm.exception.TraineeNotFoundException;
import com.epam.gymcrm.service.TraineeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TraineeControllerTest {

    private MockMvc mockMvc;
    @Mock
    private TraineeService traineeService;
    @InjectMocks
    private TraineeController traineeController;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(traineeController, new GymCrmExceptionHandler()).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetTraineeProfile() throws Exception {
        String username = "testuser";
        TraineeResponseDto responseDto = new TraineeResponseDto(null, null, username, null, null, null, null);
        when(traineeService.getByUsername(username)).thenReturn(responseDto);

        mockMvc.perform(get("/api/trainees/{username}", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username));

        verify(traineeService).getByUsername(username);
    }

    @Test
    void testGetTraineeProfile_UserNotFound() throws Exception {
        String username = "username";
        when(traineeService.getByUsername(username)).thenThrow(new TraineeNotFoundException());

        mockMvc.perform(get("/api/trainees/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(traineeService).getByUsername(username);
    }

    @Test
    void testCreateTrainee() throws Exception {
        when(traineeService.create(any())).thenReturn(null);
        TraineeRequestDto requestDto = new TraineeRequestDto("name", "lname", null, null);

        mockMvc.perform(post("/api/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());

        verify(traineeService).create(any());
    }

    @Test
    void testUpdateTraineeProfile() throws Exception {
        String username = "testuser";
        TraineeForUpdateRequestDto requestDto = new TraineeForUpdateRequestDto(username, "name", "lname", null, null, true);
        TraineeResponseDto responseDto = new TraineeResponseDto(null, null, username, null, null, null, null);
        when(traineeService.update(eq(username), any())).thenReturn(responseDto);

        mockMvc.perform(put("/api/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username));

        verify(traineeService).update(eq(username), any());
    }

    @Test
    void testDeleteTraineeProfile() throws Exception {
        String username = "testuser";

        mockMvc.perform(delete("/api/trainees/{username}", username))
                .andExpect(status().isOk());

        verify(traineeService).deleteByUsername(username);
    }

    @Test
    void testGetUnassignedTrainers() throws Exception {
        String username = "testuser";
        List<PlainTrainerResponseDto> responseDtoList = new ArrayList<>();
        when(traineeService.getUnassignedOnTraineeTrainerListByUsername(username)).thenReturn(responseDtoList);

        mockMvc.perform(get("/api/trainees/{username}/unassigned-trainers", username))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(traineeService).getUnassignedOnTraineeTrainerListByUsername(username);
    }

    @Test
    void testUpdateTrainerList() throws Exception {
        String username = "testuser";
        List<TrainerUsernameDto> trainerUsernameDtos = new ArrayList<>();

        mockMvc.perform(put("/api/trainees/{username}/trainer-list", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]"))
                .andExpect(status().isOk());

        verify(traineeService).updateTrainersList(username, trainerUsernameDtos);
    }

    @Test
    void testUpdateTraineeStatus_Activate() throws Exception {
        String username = "testuser";

        mockMvc.perform(patch("/api/trainees/{username}/status", username)
                        .param("active", "true"))
                .andExpect(status().isOk());

        verify(traineeService).activate(username);
    }

    @Test
    void testUpdateTraineeStatus_Deactivate() throws Exception {
        String username = "testuser";

        mockMvc.perform(patch("/api/trainees/{username}/status", username)
                        .param("active", "false"))
                .andExpect(status().isOk());

        verify(traineeService).deactivate(username);
    }
}
