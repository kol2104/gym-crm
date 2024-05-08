package com.epam.repository.impl;

import com.epam.model.TrainerWorkload;
import com.epam.model.Year;
import com.epam.repository.TrainerWorkloadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class TrainerWorkloadMongoRepository implements TrainerWorkloadRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Optional<TrainerWorkload> getByUsername(String username) {
        return Optional.ofNullable(mongoTemplate.findById(username, TrainerWorkload.class));
    }

    @Override
    public void persistTrainerWorkload(TrainerWorkload trainerWorkload) {
        if (trainerWorkload == null || trainerWorkload.getUsername() == null) {
            return;
        }
        if (trainerWorkload.getYears() == null) {
            List<Year> years = new ArrayList<>();
            trainerWorkload.setYears(years);
        }
        mongoTemplate.save(trainerWorkload);
    }
}
