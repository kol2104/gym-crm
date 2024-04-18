package com.epam.gymcrm.controller;

import com.epam.gymcrm.controller.handler.GymCrmExceptionHandler;
import com.epam.gymcrm.dto.user.UserNewPasswordRequestDto;
import com.epam.gymcrm.exception.UserNotFoundException;
import com.epam.gymcrm.service.UserService;
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

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {

    private AutoCloseable mocksClose;
    private MockMvc mockMvc;
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mocksClose = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController, new GymCrmExceptionHandler()).build();
        objectMapper = new ObjectMapper();
    }

    @AfterEach
    public void close() throws Exception {
        mocksClose.close();
    }

    @Test
    void testChangePassword() throws Exception {
        String token = "some-token";
        UserNewPasswordRequestDto newPasswordRequestDto = new UserNewPasswordRequestDto("oldPassword", "newPassword");

        mockMvc.perform(put("/api/users/password")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPasswordRequestDto)))
                .andExpect(status().isOk());

        verify(userService).updatePassword(newPasswordRequestDto, token);
    }

    @Test
    void testChangePassword_UserNotFoundException() throws Exception {
        String token = "some-token";
        UserNewPasswordRequestDto newPasswordRequestDto = new UserNewPasswordRequestDto("oldPassword", "newPassword");

        doThrow(new UserNotFoundException("User not found")).when(userService).updatePassword(newPasswordRequestDto, token);

        mockMvc.perform(put("/api/users/password")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"oldPassword\":\"oldPassword\",\"newPassword\":\"newPassword\"}"))
                .andExpect(status().isNotFound());
    }
}
