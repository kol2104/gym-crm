package com.epam.gymcrm.service.impl;

import com.epam.gymcrm.dao.TrainerDao;
import com.epam.gymcrm.dao.TrainingTypeDao;
import com.epam.gymcrm.dao.UserDao;
import com.epam.gymcrm.dto.user.UserCredentialsDto;
import com.epam.gymcrm.dto.trainer.TrainerForUpdateRequestDto;
import com.epam.gymcrm.dto.trainer.TrainerRequestDto;
import com.epam.gymcrm.dto.trainer.TrainerResponseDto;
import com.epam.gymcrm.exception.TrainerNotFoundException;
import com.epam.gymcrm.exception.TrainingTypeNotFoundException;
import com.epam.gymcrm.mapper.TrainerMapper;
import com.epam.gymcrm.model.Trainer;
import com.epam.gymcrm.service.TrainerService;
import com.epam.gymcrm.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final UserDao userDao;
    private final TrainerDao trainerDao;
    private final TrainerMapper trainerMapper;
    private final TrainingTypeDao trainingTypeDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserCredentialsDto create(TrainerRequestDto trainerRequestDto) {
        Trainer trainer = trainerMapper.dtoToModel(trainerRequestDto);
        log.info("Saving trainer: {}", trainer);
        trainer.setUsername(getUsername(trainer.getFirstName(), trainer.getLastName()));
        String password = PasswordUtil.getRandomPassword(10);
        trainer.setPassword(passwordEncoder.encode(password));
        trainer.setActive(true);
        trainer.setSpecialization(trainingTypeDao.getById(trainer.getSpecialization().getId()).orElseThrow(() -> {
            log.error("Training type with id {} not found.", trainer.getSpecialization().getId());
            return new TrainingTypeNotFoundException(trainer.getSpecialization().getId());
        }));
        Trainer savedTrainer = trainerDao.create(trainer);
        savedTrainer.setPassword(password);
        return trainerMapper.modelToCredentialsDto(savedTrainer);
    }

    @Override
    public TrainerResponseDto getByUsername(String username) {
        log.info("Finding trainer by username: {}", username);
        return trainerMapper.modelToDto(trainerDao.getByUsername(username)
                .orElseThrow(() -> {
                    log.error("Trainer with username {} not found.", username);
                    return new TrainerNotFoundException(username);
                }));
    }

    @Override
    public TrainerResponseDto update(String username, TrainerForUpdateRequestDto updateTrainerRequestDto) {
        Trainer trainer = trainerMapper.dtoToModel(updateTrainerRequestDto);
        log.info("Updating trainer with username {}", username);
        Trainer trainerFromDatabase = trainerDao.getByUsername(username).orElseThrow(() -> {
            log.error("Trainer with username {} not found.", username);
            return new TrainerNotFoundException(username);
        });
        trainer.setSpecialization(trainingTypeDao.getById(updateTrainerRequestDto.specializationId()).orElseThrow(() -> {
            log.error("Training type with id {} not found.", updateTrainerRequestDto.specializationId());
            return new TrainingTypeNotFoundException(updateTrainerRequestDto.specializationId());
        }));
        trainer.setId(trainerFromDatabase.getId());
        trainer.setUsername(username);
        trainer.setPassword(trainerFromDatabase.getPassword());
        trainer.setTrainees(trainerFromDatabase.getTrainees());
        return trainerMapper.modelToDto(trainerDao.update(trainer));
    }

    @Override
    public void activate(String username) {
        log.info("Activating trainer with username {}", username);
        Trainer foundTrainee = trainerDao.getByUsername(username).orElseThrow(() -> {
            log.error("Trainer with username {} not found.", username);
            return new TrainerNotFoundException(username);
        });
        if (foundTrainee.isActive()) {
            log.debug("Trainer already is activated");
            return;
        }
        foundTrainee.setActive(true);
        trainerDao.update(foundTrainee);
    }

    @Override
    public void deactivate(String username) {
        log.info("Deactivating trainer with username {}", username);
        Trainer foundTrainee = trainerDao.getByUsername(username).orElseThrow(() -> {
            log.error("Trainer with username {} not found.", username);
            return new TrainerNotFoundException(username);
        });
        if (!foundTrainee.isActive()) {
            log.debug("Trainer already is deactivated");
            return;
        }
        foundTrainee.setActive(false);
        trainerDao.update(foundTrainee);
    }

    private String getUsername(String firstName, String lastName) {
        String username = firstName + "." + lastName;
        if (userDao.getByFirstNameAndLastName(firstName, lastName).isPresent()) {
            return username + "_" + System.currentTimeMillis();
        }
        return username;
    }
}
