package com.epam.gymcrm.controller;

import com.epam.gymcrm.controller.handler.GymCrmExceptionHandler;
import com.epam.gymcrm.dto.TrainingTypeDto;
import com.epam.gymcrm.service.TrainingTypeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TrainingTypeControllerTest {

    private AutoCloseable mocksClose;
    private MockMvc mockMvc;
    @Mock
    private TrainingTypeService trainingTypeService;
    @InjectMocks
    private TrainingTypeController trainingTypeController;

    @BeforeEach
    public void setUp() {
        mocksClose = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(trainingTypeController, new GymCrmExceptionHandler()).build();
    }

    @AfterEach
    public void close() throws Exception {
        mocksClose.close();
    }

    @Test
    void testGetAll() throws Exception {
        List<TrainingTypeDto> trainingTypes = List.of(new TrainingTypeDto(1L, "name"), new TrainingTypeDto(2L, "name2"));
        when(trainingTypeService.getAll()).thenReturn(trainingTypes);

        mockMvc.perform(get("/api/training-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(trainingTypes.size()));

        verify(trainingTypeService).getAll();
    }
}
