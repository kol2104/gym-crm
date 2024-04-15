package com.epam.gymcrm.service.impl;

import com.epam.gymcrm.auth.Authentication;
import com.epam.gymcrm.dao.TraineeDao;
import com.epam.gymcrm.dao.TrainerDao;
import com.epam.gymcrm.dao.UserDao;
import com.epam.gymcrm.dto.TokenDto;
import com.epam.gymcrm.dto.user.UserCredentialsDto;
import com.epam.gymcrm.dto.user.UserNewPasswordRequestDto;
import com.epam.gymcrm.exception.TraineeNotFoundException;
import com.epam.gymcrm.exception.UserNotFoundException;
import com.epam.gymcrm.model.Role;
import com.epam.gymcrm.model.User;
import com.epam.gymcrm.service.UserService;
import com.epam.gymcrm.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final TraineeDao traineeDao;
    private final TrainerDao trainerDao;
    private final JwtUtil jwtUtil;

    @Override
    public TokenDto authenticateUser(UserCredentialsDto userCredentialsDto) {
        log.info("Authenticate user with username '{}' and password", userCredentialsDto.username());
        Role userRole = getUserRole(userCredentialsDto);
        if (userRole == null) {
            throw new UserNotFoundException();
        }
        String token = buildToken(userCredentialsDto.username(), userRole);
        return TokenDto.builder().token(token).build();
    }

    private Role getUserRole(UserCredentialsDto userCredentialsDto) {
        boolean isUserTrainee = traineeDao.getByUsernameAndPassword(
                userCredentialsDto.username(), userCredentialsDto.password()
        ).isPresent();
        if (isUserTrainee) {
            return Role.TRAINEE;
        }
        boolean isUserTrainer = trainerDao.getByUsernameAndPassword(
                userCredentialsDto.username(), userCredentialsDto.password()
        ).isPresent();
        if (isUserTrainer) {
            return Role.TRAINER;
        }
        return null;
    }

    private String buildToken(String username, Role role) {
        return jwtUtil.generateToken(
                Authentication.builder()
                        .username(username)
                        .role(role)
                        .build()
        );
    }

    @Override
    public void updatePassword(UserNewPasswordRequestDto newPasswordDto, String token) {
        Authentication authentication = jwtUtil.validateToken(token);
        log.info("Updating trainee's password with username {}", authentication.username());
        User foundUser = userDao.getByUsernameAndPassword(authentication.username(), newPasswordDto.oldPassword())
                .orElseThrow(() -> {
                    log.error("User with username {} and password not found.", authentication.username());
                    return new UserNotFoundException(authentication.username());
                });
        foundUser.setPassword(newPasswordDto.newPassword());
        userDao.update(foundUser);
    }

}
