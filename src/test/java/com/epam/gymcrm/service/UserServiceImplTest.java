package com.epam.gymcrm.service;

import com.epam.gymcrm.auth.Authentication;
import com.epam.gymcrm.dao.TraineeDao;
import com.epam.gymcrm.dao.TrainerDao;
import com.epam.gymcrm.dao.UserDao;
import com.epam.gymcrm.dto.TokenDto;
import com.epam.gymcrm.dto.user.UserCredentialsDto;
import com.epam.gymcrm.dto.user.UserNewPasswordRequestDto;
import com.epam.gymcrm.exception.UserNotFoundException;
import com.epam.gymcrm.model.Trainee;
import com.epam.gymcrm.model.Trainer;
import com.epam.gymcrm.model.User;
import com.epam.gymcrm.service.impl.UserServiceImpl;
import com.epam.gymcrm.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;
    @Mock
    private TraineeDao traineeDao;
    @Mock
    private TrainerDao trainerDao;
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testAuthenticateUser_Trainee() {
        // Given
        UserCredentialsDto userCredentialsDto = new UserCredentialsDto("traineeUsername", "traineePassword");
        when(traineeDao.getByUsernameAndPassword(userCredentialsDto.username(), userCredentialsDto.password()))
                .thenReturn(Optional.of(new Trainee()));
        when(jwtUtil.generateToken(any())).thenReturn("testToken");

        // When
        TokenDto tokenDto = userService.authenticateUser(userCredentialsDto);

        // Then
        assertEquals("testToken", tokenDto.token());
    }

    @Test
    void testAuthenticateUser_Trainer() {
        // Given
        UserCredentialsDto userCredentialsDto = new UserCredentialsDto("trainerUsername", "trainerPassword");
        when(trainerDao.getByUsernameAndPassword(userCredentialsDto.username(), userCredentialsDto.password()))
                .thenReturn(Optional.of(new Trainer()));
        when(jwtUtil.generateToken(any())).thenReturn("testToken");

        // When
        TokenDto tokenDto = userService.authenticateUser(userCredentialsDto);

        // Then
        assertEquals("testToken", tokenDto.token());
    }

    @Test
    void testAuthenticateUser_UserNotFound() {
        // Given
        UserCredentialsDto userCredentialsDto = new UserCredentialsDto("nonExistentUsername", "password");

        // When/Then
        assertThrows(UserNotFoundException.class, () -> userService.authenticateUser(userCredentialsDto));
    }

    @Test
    void testUpdatePassword() {
        // Given
        String token = "testToken";
        UserNewPasswordRequestDto newPasswordDto = new UserNewPasswordRequestDto("oldPassword", "newPassword");
        when(jwtUtil.validateToken(token)).thenReturn(Authentication.builder().username("username").build());
        when(userDao.getByUsernameAndPassword("username", newPasswordDto.oldPassword()))
                .thenReturn(Optional.of(new User()));

        // When
        userService.updatePassword(newPasswordDto, token);

        // Then - verify that userDao.update() is called
        verify(userDao).update(any(User.class));
    }

    @Test
    void testUpdatePassword_UserNotFound() {
        // Given
        String token = "testToken";
        UserNewPasswordRequestDto newPasswordDto = new UserNewPasswordRequestDto("oldPassword", "newPassword");
        Authentication authentication = Authentication.builder().username("username").build();
        when(jwtUtil.validateToken(token)).thenReturn(authentication);
        when(userDao.getByUsernameAndPassword(authentication.username(), newPasswordDto.oldPassword()))
                .thenReturn(Optional.empty());

        // When/Then
        assertThrows(UserNotFoundException.class, () -> userService.updatePassword(newPasswordDto, token));
        // Verify that userDao.update() is not called
        verify(userDao, never()).update(any());
    }
}
