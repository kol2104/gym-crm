package com.epam.gymcrm.controller;

import com.epam.gymcrm.GymCrmApplication;
import com.epam.gymcrm.auth.JwtAuthenticationFilter;
import com.epam.gymcrm.common.Constants;
import com.epam.gymcrm.config.SpringSecurityConfiguration;
import com.epam.gymcrm.controller.handler.GymCrmExceptionHandler;
import com.epam.gymcrm.dto.user.UserNewPasswordRequestDto;
import com.epam.gymcrm.exception.UserNotFoundException;
import com.epam.gymcrm.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserControllerTest {
    @Autowired
    private WebApplicationContext context;


    private AutoCloseable mocksClose;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setUp() {
        mocksClose = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        objectMapper = new ObjectMapper();
    }

    @AfterEach
    public void close() throws Exception {
        mocksClose.close();
    }

    @Test
    @WithMockUser(username = "user")
    void testChangePassword() throws Exception {
        UserNewPasswordRequestDto newPasswordRequestDto = new UserNewPasswordRequestDto("oldPassword", "newPassword");

        mockMvc.perform(put("/api/users/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPasswordRequestDto)))
                .andExpect(status().isOk());

        verify(userService).updatePassword(newPasswordRequestDto, "user");
    }

    @Test
    @WithMockUser(username = "user")
    void testChangePassword_UserNotFoundException() throws Exception {
        UserNewPasswordRequestDto newPasswordRequestDto = new UserNewPasswordRequestDto("oldPassword", "newPassword");

        doThrow(new UserNotFoundException("User not found")).when(userService).updatePassword(newPasswordRequestDto, "user");

        mockMvc.perform(put("/api/users/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"oldPassword\":\"oldPassword\",\"newPassword\":\"newPassword\"}"))
                .andExpect(status().isNotFound());
    }
}
