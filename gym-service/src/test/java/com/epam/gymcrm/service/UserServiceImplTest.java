package com.epam.gymcrm.service;

import com.epam.gymcrm.dao.UserDao;
import com.epam.gymcrm.dto.TokenDto;
import com.epam.gymcrm.dto.user.UserCredentialsDto;
import com.epam.gymcrm.dto.user.UserNewPasswordRequestDto;
import com.epam.gymcrm.exception.TooManyLoginAttemptsException;
import com.epam.gymcrm.exception.UserNotFoundException;
import com.epam.gymcrm.model.User;
import com.epam.gymcrm.service.impl.UserServiceImpl;
import com.epam.gymcrm.util.JwtUtil;
import com.epam.gymcrm.service.security.LoginAttemptService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private LoginAttemptService loginAttemptService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testAuthenticateUser_Success() {
        UserCredentialsDto userCredentialsDto = new UserCredentialsDto("user", "pass");
        when(loginAttemptService.isBlocked(anyString())).thenReturn(false);
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(mock(Authentication.class));
        when(jwtUtil.generateToken(anyString(), any())).thenReturn("token");

        TokenDto result = userService.authenticateUser(userCredentialsDto);

        assertEquals("token", result.token());
        verify(loginAttemptService).loginSucceeded(userCredentialsDto.username());
    }

    @Test
    void testAuthenticateUser_UserBlocked() {
        UserCredentialsDto userCredentialsDto = new UserCredentialsDto("user", "pass");
        when(loginAttemptService.isBlocked(userCredentialsDto.username())).thenReturn(true);

        assertThrows(TooManyLoginAttemptsException.class, () -> userService.authenticateUser(userCredentialsDto));
    }

    @Test
    void testAuthenticateUser_BadCredentials() {
        UserCredentialsDto userCredentialsDto = new UserCredentialsDto("user", "pass");
        when(loginAttemptService.isBlocked(anyString())).thenReturn(false);
        when(authenticationManager.authenticate(any(Authentication.class))).thenThrow(BadCredentialsException.class);

        assertThrows(AuthenticationException.class, () -> userService.authenticateUser(userCredentialsDto));
        verify(loginAttemptService).loginFailed(userCredentialsDto.username());
    }

    @Test
    void testUpdatePassword_Success() {
        UserNewPasswordRequestDto newPasswordDto = new UserNewPasswordRequestDto("oldPass", "newPass");
        User user = new User();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("oldPass"));

        when(userDao.getByUsername("user")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPass", user.getPassword())).thenReturn(true);

        userService.updatePassword(newPasswordDto, "user");

        verify(userDao).update(user);
        assertEquals(user.getPassword(), passwordEncoder.encode("newPass"));
    }

    @Test
    void testUpdatePassword_UserNotFoundOrOldPasswordInvalid() {
        UserNewPasswordRequestDto newPasswordDto = new UserNewPasswordRequestDto("oldPass", "newPass");

        when(userDao.getByUsername("user")).thenReturn(Optional.empty()); // User not found
        assertThrows(UserNotFoundException.class, () -> userService.updatePassword(newPasswordDto, "user"));

        User user = new User();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("oldPass"));
        when(userDao.getByUsername("user")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPass", user.getPassword())).thenReturn(false); // Old password does not match

        assertThrows(UserNotFoundException.class, () -> userService.updatePassword(newPasswordDto, "user"));
    }
}
