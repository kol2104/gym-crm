package com.epam.gymcrm.controller;

import com.epam.gymcrm.controller.handler.GymCrmExceptionHandler;
import com.epam.gymcrm.dto.TokenDto;
import com.epam.gymcrm.dto.user.UserCredentialsDto;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthenticationControllerTest {

    private AutoCloseable mocksClose;
    private MockMvc mockMvc;
    @Mock
    private UserService userService;
    @InjectMocks
    private AuthenticationController authenticationController;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mocksClose = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController, new GymCrmExceptionHandler()).build();
        objectMapper = new ObjectMapper();
    }

    @AfterEach
    public void close() throws Exception {
        mocksClose.close();
    }

    @Test
    void testLogin_Success() throws Exception {
        UserCredentialsDto credentialsDto = new UserCredentialsDto("username", "password");
        TokenDto tokenDto = new TokenDto("testToken");
        when(userService.authenticateUser(credentialsDto)).thenReturn(tokenDto);

        mockMvc.perform(get("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credentialsDto)))
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"));
    }

    @Test
    void testLogin_UserNotFound() throws Exception {
        UserCredentialsDto credentialsDto = new UserCredentialsDto("nonexistentUser", "password");
        when(userService.authenticateUser(credentialsDto)).thenThrow(new UserNotFoundException());

        mockMvc.perform(get("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credentialsDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testLogin_BadRequest() throws Exception {
        UserCredentialsDto credentialsDto = new UserCredentialsDto(null, "password");
        when(userService.authenticateUser(credentialsDto)).thenThrow(new UserNotFoundException());

        mockMvc.perform(get("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credentialsDto)))
                .andExpect(status().isBadRequest());
    }
}
