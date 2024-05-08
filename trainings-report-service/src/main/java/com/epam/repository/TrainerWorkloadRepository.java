package com.epam.repository;

import com.epam.model.TrainerWorkload;

import java.util.Optional;

public interface TrainerWorkloadRepository {
    Optional<TrainerWorkload> getByUsername(String username);
    void persistTrainerWorkload(TrainerWorkload trainerWorkload);
}
