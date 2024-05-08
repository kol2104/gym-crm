package com.epam.gymcrm.service.impl;

import com.epam.gymcrm.dao.TraineeDao;
import com.epam.gymcrm.dao.TrainerDao;
import com.epam.gymcrm.dao.UserDao;
import com.epam.gymcrm.dto.trainee.TraineeForUpdateRequestDto;
import com.epam.gymcrm.dto.trainee.TraineeRequestDto;
import com.epam.gymcrm.dto.trainee.TraineeResponseDto;
import com.epam.gymcrm.dto.trainer.PlainTrainerResponseDto;
import com.epam.gymcrm.dto.trainer.TrainerUsernameDto;
import com.epam.gymcrm.dto.user.UserCredentialsDto;
import com.epam.gymcrm.exception.TraineeNotFoundException;
import com.epam.gymcrm.mapper.TraineeMapper;
import com.epam.gymcrm.mapper.TrainerMapper;
import com.epam.gymcrm.model.Trainee;
import com.epam.gymcrm.model.Trainer;
import com.epam.gymcrm.service.TraineeService;
import com.epam.gymcrm.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TraineeServiceImpl implements TraineeService {

    private final TraineeDao traineeDao;
    private final TrainerDao trainerDao;
    private final UserDao userDao;
    private final TraineeMapper traineeMapper;
    private final TrainerMapper trainerMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserCredentialsDto create(TraineeRequestDto traineeRequestDto) {
        Trainee trainee = traineeMapper.dtoToModel(traineeRequestDto);
        log.info("Saving trainee: {}", trainee);
        trainee.setUsername(getUsername(trainee.getFirstName(), trainee.getLastName()));
        String password = PasswordUtil.getRandomPassword(10);
        trainee.setPassword(passwordEncoder.encode(password));
        trainee.setActive(true);
        Trainee savedTrainee = traineeDao.create(trainee);
        savedTrainee.setPassword(password);
        return traineeMapper.modelToCredentialsDto(savedTrainee);
    }

    @Override
    public TraineeResponseDto getByUsername(String username) {
        log.info("Finding trainee by username: {}", username);
        return traineeMapper.modelToDto(
                traineeDao.getByUsername(username)
                        .orElseThrow(() -> {
                            log.error("Trainee with username {} not found.", username);
                            return new TraineeNotFoundException(username);
                        })
        );
    }

    @Override
    public void deleteByUsername(String username) {
        log.info("Deleting trainee with username: {}", username);
        traineeDao.deleteByUsername(username);
    }

    @Override
    public TraineeResponseDto update(String username, TraineeForUpdateRequestDto updateTraineeRequestDto) {
        Trainee trainee = traineeMapper.dtoToModel(updateTraineeRequestDto);
        log.info("Updating trainee with username {}", username);
        Trainee traineeFromDatabase = traineeDao.getByUsername(username).orElseThrow(() -> {
            log.error("Trainee with username {} not found.", username);
            return new TraineeNotFoundException(username);
        });
        trainee.setId(traineeFromDatabase.getId());
        trainee.setUsername(username);
        trainee.setPassword(traineeFromDatabase.getPassword());
        trainee.setTrainers(traineeFromDatabase.getTrainers());
        return traineeMapper.modelToDto(traineeDao.update(trainee));
    }

    @Override
    public void updateTrainersList(String username, List<TrainerUsernameDto> trainerUsernameDtos) {
        log.info("Updating trainee's trainers list with username {}", username);
        Trainee foundTrainee = traineeDao.getByUsername(username).orElseThrow(() -> {
            log.error("Trainee with username {} not found.", username);
            return new TraineeNotFoundException(username);
        });
        List<Trainer> trainers = trainerDao.getAllByUsernames(
                trainerUsernameDtos.stream()
                        .map(TrainerUsernameDto::username)
                        .toList()
        );
        foundTrainee.setTrainers(trainers);
        traineeDao.update(foundTrainee);
    }

    @Override
    public List<PlainTrainerResponseDto> getUnassignedOnTraineeTrainerListByUsername(String username) {
        log.info("Retrieving trainers list that not assigned on trainee by trainee's username.");
        if (traineeDao.getByUsername(username).isEmpty()) {
            log.error("Trainee with username {} not found.", username);
            throw new TraineeNotFoundException(username);
        }
        return traineeDao.getUnassignedOnTraineeTrainerListByUsername(username).stream()
                .map(trainerMapper::modelToPlainDto)
                .toList();
    }

    @Override
    public void activate(String username) {
        log.info("Activating trainee with username {}", username);
        Trainee foundTrainee = traineeDao.getByUsername(username).orElseThrow(() -> {
            log.error("Trainee with username {} not found.", username);
            return new TraineeNotFoundException(username);
        });
        if (foundTrainee.isActive()) {
            log.debug("Trainee already is activated");
            return;
        }
        foundTrainee.setActive(true);
        traineeDao.update(foundTrainee);
    }

    @Override
    public void deactivate(String username) {
        log.info("Deactivating trainee with username {}", username);
        Trainee foundTrainee = traineeDao.getByUsername(username).orElseThrow(() -> {
            log.error("Trainee with username {} not found.", username);
            return new TraineeNotFoundException(username);
        });
        if (!foundTrainee.isActive()) {
            log.debug("Trainee already is deactivated");
            return;
        }
        foundTrainee.setActive(false);
        traineeDao.update(foundTrainee);
    }

    private String getUsername(String firstName, String lastName) {
        String username = firstName + "." + lastName;
        if (userDao.getByFirstNameAndLastName(firstName, lastName).isPresent()) {
            return username + "_" + System.currentTimeMillis();
        }
        return username;
    }
}
