package com.epam.gymcrm.dao.impl;

import com.epam.gymcrm.dao.TrainingTypeDao;
import com.epam.gymcrm.model.Trainee;
import com.epam.gymcrm.model.Trainer;
import com.epam.gymcrm.model.TrainingType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class TrainingTypeDaoDatabase implements TrainingTypeDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    @Override
    public Optional<TrainingType> getById(Long id) {
        TrainingType trainingType = entityManager.find(TrainingType.class, id);
        if (trainingType != null) {
            log.debug("Found training type by id {}", id);
        } else {
            log.debug("Training type with id {} not found.", id);
        }
        return Optional.ofNullable(trainingType);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<TrainingType> getByName(String name) {
        Optional<TrainingType> foundTrainingType = entityManager
                .createQuery("from TrainingType t where t.name = :name", TrainingType.class)
                .setParameter("name", name)
                .getResultStream().findFirst();
        if (foundTrainingType.isPresent()) {
            log.debug("Found training type by name '{}'", name);
        } else {
            log.debug("Training type with name '{}'.", name);
        }
        return foundTrainingType;
    }

    @Transactional(readOnly = true)
    @Override
    public List<TrainingType> getAll() {
        List<TrainingType> allTrainingTypes = entityManager.createQuery("from TrainingType", TrainingType.class)
                .getResultList();
        log.debug("Found {} training types.", allTrainingTypes.size());
        return allTrainingTypes;
    }
}
