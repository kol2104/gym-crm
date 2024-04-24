package com.epam.gymcrm.service.security;

import com.epam.gymcrm.dao.TraineeDao;
import com.epam.gymcrm.dao.TrainerDao;
import com.epam.gymcrm.model.Role;
import com.epam.gymcrm.model.Trainee;
import com.epam.gymcrm.model.Trainer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final TraineeDao traineeDao;
    private final TrainerDao trainerDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Trainee> optionalTrainee = traineeDao.getByUsername(username);
        if (optionalTrainee.isPresent()) {
            return User.builder()
                    .username(username)
                    .password(optionalTrainee.get().getPassword())
                    .authorities(Role.TRAINEE.getRoleName())
                    .build();
        }
        Optional<Trainer> optionalTrainer = trainerDao.getByUsername(username);
        if (optionalTrainer.isPresent()) {
            return User.builder()
                    .username(username)
                    .password(optionalTrainer.get().getPassword())
                    .authorities(Role.TRAINER.getRoleName())
                    .build();
        }
        throw new UsernameNotFoundException(username);
    }
}
