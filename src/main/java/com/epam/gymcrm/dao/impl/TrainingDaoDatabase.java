package com.epam.gymcrm.dao.impl;

import com.epam.gymcrm.dao.TrainingDao;
import com.epam.gymcrm.model.Training;
import com.epam.gymcrm.model.TrainingCriteria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class TrainingDaoDatabase implements TrainingDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public Training create(Training training) {
        entityManager.persist(training);
        log.info("Training created successfully: {}", training);
        return training;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Training> getAll() {
        List<Training> allTrainings = entityManager.createQuery("from Training", Training.class).getResultList();
        log.debug("Found {} trainings.", allTrainings.size());
        return allTrainings;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Training> getById(Long id) {
        Training training = entityManager.find(Training.class, id);
        if (training != null) {
            log.debug("Found training by id {}: {}", id, training);
        } else {
            log.debug("Training with id {} not found.", id);
        }
        return Optional.ofNullable(training);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Training> getTrainingsByCriteria(Map<TrainingCriteria, String> criteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> query = criteriaBuilder.createQuery(Training.class);
        Root<Training> root = query.from(Training.class);
        root.fetch("trainee", JoinType.LEFT);
        root.fetch("trainer", JoinType.LEFT);
        root.fetch("trainingType", JoinType.LEFT);

        Predicate predicate = buildPredicate(criteriaBuilder, root, criteria);

        query.select(root).where(predicate);

        List<Training> trainings = entityManager.createQuery(query).getResultList();
        log.debug("Found {} trainings", trainings.size());
        return trainings;
    }

    private Predicate buildPredicate(CriteriaBuilder criteriaBuilder, Root<Training> root,
                                     Map<TrainingCriteria, String> criteria) {
        Predicate predicate = criteriaBuilder.conjunction();

        for (Map.Entry<TrainingCriteria, String> entry : criteria.entrySet()) {
            Predicate condition = TrainingCriteria.getPredicateForCriterion(
                    entry.getKey(), criteriaBuilder, root, entry.getValue()
            );
            predicate = criteriaBuilder.and(predicate, condition);
        }
        return predicate;
    }
}
