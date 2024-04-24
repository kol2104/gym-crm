package com.epam.gymcrm.service.impl;

import com.epam.gymcrm.dao.UserDao;
import com.epam.gymcrm.dto.TokenDto;
import com.epam.gymcrm.dto.user.UserCredentialsDto;
import com.epam.gymcrm.dto.user.UserNewPasswordRequestDto;
import com.epam.gymcrm.exception.TooManyLoginAttemptsException;
import com.epam.gymcrm.exception.UserNotFoundException;
import com.epam.gymcrm.model.User;
import com.epam.gymcrm.service.security.LoginAttemptService;
import com.epam.gymcrm.service.UserService;
import com.epam.gymcrm.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final LoginAttemptService loginAttemptService;

    @Override
    public TokenDto authenticateUser(UserCredentialsDto userCredentialsDto) {
        if (loginAttemptService.isBlocked(userCredentialsDto.username())) {
            throw new TooManyLoginAttemptsException();
        }
        log.info("Authenticate user with username '{}' and password", userCredentialsDto.username());
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userCredentialsDto.username(), userCredentialsDto.password())
            );
        } catch (AuthenticationException exception) {
            loginAttemptService.loginFailed(userCredentialsDto.username());
            throw exception;
        }
        loginAttemptService.loginSucceeded(userCredentialsDto.username());
        String token = jwtUtil.generateToken(userCredentialsDto.username(), authentication.getAuthorities());
        return TokenDto.builder().token(token).build();
    }

    @Override
    public void updatePassword(UserNewPasswordRequestDto newPasswordDto, String username) {
        log.info("Updating trainee's password with username {}", username);
        Optional<User> optionalUser = userDao.getByUsername(username);
        if (optionalUser.isEmpty() || !passwordEncoder.matches(newPasswordDto.oldPassword(), optionalUser.get().getPassword())) {
            log.error("User with username {} and password not found.", username);
            throw new UserNotFoundException(username);
        }
        User foundUser = optionalUser.get();
        foundUser.setPassword(passwordEncoder.encode(newPasswordDto.newPassword()));
        userDao.update(foundUser);
    }

}
