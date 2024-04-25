package com.epam.gymcrm.dao.impl;

import com.epam.gymcrm.dao.TrainerDao;
import com.epam.gymcrm.model.Trainer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class TrainerDaoDatabase implements TrainerDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public Trainer create(Trainer trainer) {
        entityManager.persist(trainer);
        log.info("Trainer created successfully: {}", trainer);
        return trainer;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Trainer> getAllByUsernames(List<String> usernames) {
        List<Trainer> allTrainers = entityManager
                .createQuery("from Trainer t where t.username in (:usernames)", Trainer.class)
                .setParameter("usernames", usernames)
                .getResultList();
        log.debug("Found {} trainers.", allTrainers.size());
        return allTrainers;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Trainer> getByUsername(String username) {
        Optional<Trainer> foundTrainer = entityManager
                .createQuery("from Trainer t left join fetch t.trainees left join fetch t.specialization where t.username = :username", Trainer.class)
                .setParameter("username", username)
                .getResultStream().findFirst();
        if (foundTrainer.isPresent()) {
            log.debug("Found trainer by username '{}'", username);
        } else {
            log.debug("Trainer with username '{}'.", username);
        }
        return foundTrainer;
    }

    @Transactional
    @Override
    public Trainer update(Trainer trainer) {
        if (entityManager.find(Trainer.class, trainer.getId()) == null) {
            log.debug("Trainer with id {} not found. Unable to update.", trainer.getId());
            return null;
        }
        entityManager.merge(trainer);
        log.info("Trainer updated successfully: {}", trainer);
        return trainer;
    }
}
