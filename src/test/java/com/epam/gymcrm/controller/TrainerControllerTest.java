package com.epam.gymcrm.controller;

import com.epam.gymcrm.controller.handler.GymCrmExceptionHandler;
import com.epam.gymcrm.dto.trainer.TrainerForUpdateRequestDto;
import com.epam.gymcrm.dto.trainer.TrainerRequestDto;
import com.epam.gymcrm.dto.trainer.TrainerResponseDto;
import com.epam.gymcrm.dto.user.UserCredentialsDto;
import com.epam.gymcrm.exception.TrainerNotFoundException;
import com.epam.gymcrm.service.TrainerService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TrainerControllerTest {

    private AutoCloseable mocksClose;
    private MockMvc mockMvc;
    @Mock
    private TrainerService trainerService;
    @InjectMocks
    private TrainerController trainerController;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mocksClose = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(trainerController, new GymCrmExceptionHandler()).build();
        objectMapper = new ObjectMapper();
    }

    @AfterEach
    public void close() throws Exception {
        mocksClose.close();
    }

    @Test
    void testGetTrainerProfile() throws Exception {
        String username = "testuser";
        TrainerResponseDto trainerResponseDto = new TrainerResponseDto(null, null, username, null, null, null);
        when(trainerService.getByUsername(username)).thenReturn(trainerResponseDto);

        mockMvc.perform(get("/api/trainers/{username}", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username));

        verify(trainerService).getByUsername(username);
    }

    @Test
    void testGetTraineeProfile_UserNotFound() throws Exception {
        String username = "username";
        when(trainerService.getByUsername(username)).thenThrow(new TrainerNotFoundException(username));

        mockMvc.perform(get("/api/trainers/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(trainerService).getByUsername(username);
    }

    @Test
    void testCreateTrainer() throws Exception {
        TrainerRequestDto trainerRequestDto = new TrainerRequestDto("name", "lname", 1L);
        UserCredentialsDto userCredentialsDto = new UserCredentialsDto("testuser", "pass");
        when(trainerService.create(any(TrainerRequestDto.class))).thenReturn(userCredentialsDto);

        mockMvc.perform(post("/api/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("testuser"));

        verify(trainerService).create(any(TrainerRequestDto.class));
    }

    @Test
    void testUpdateTrainerProfile() throws Exception {
        String username = "testuser";
        TrainerForUpdateRequestDto trainerForUpdateRequestDto = new TrainerForUpdateRequestDto(username, "name", "lname", 1L, true);
        TrainerResponseDto trainerResponseDto = new TrainerResponseDto(null, null, username, null, null, null);
        when(trainerService.update(username, trainerForUpdateRequestDto)).thenReturn(trainerResponseDto);

        mockMvc.perform(put("/api/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerForUpdateRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username));

        verify(trainerService).update(username, trainerForUpdateRequestDto);
    }

    @Test
    void testUpdateTrainerStatusToTrue() throws Exception {
        String username = "testuser";
        boolean isActive = true;

        mockMvc.perform(patch("/api/trainers/{username}/status", username)
                        .param("active", String.valueOf(isActive)))
                .andExpect(status().isOk());

        verify(trainerService).activate(username);
    }

    @Test
    void testUpdateTrainerStatusToFalse() throws Exception {
        String username = "testuser";
        boolean isActive = false;

        mockMvc.perform(patch("/api/trainers/{username}/status", username)
                        .param("active", String.valueOf(isActive)))
                .andExpect(status().isOk());

        verify(trainerService).deactivate(username);
    }
}
