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
    public List<Trainer> getAll() {
        List<Trainer> allTrainers = entityManager.createQuery("from Trainer", Trainer.class).getResultList();
        log.debug("Found {} trainers.", allTrainers.size());
        return allTrainers;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Trainer> getById(Long id) {
        Trainer trainer = entityManager.find(Trainer.class, id);
        if (trainer != null) {
            log.debug("Found trainer by id {}: {}", id, trainer);
        } else {
            log.debug("Trainer with id {} not found.", id);
        }
        return Optional.ofNullable(trainer);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Trainer> getByUsername(String username) {
        Optional<Trainer> foundTrainer = entityManager
                .createQuery("from Trainer t where t.username = :username", Trainer.class)
                .setParameter("username", username)
                .getResultStream().findFirst();
        if (foundTrainer.isPresent()) {
            log.debug("Found trainer by username '{}'", username);
        } else {
            log.debug("Trainer with username '{}'.", username);
        }
        return foundTrainer;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Trainer> getByUsernameAndPassword(String username, String password) {
        Optional<Trainer> foundTrainer = entityManager
                .createQuery("from Trainer t where t.username = :username and t.password = :password", Trainer.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .getResultStream().findFirst();
        if (foundTrainer.isPresent()) {
            log.debug("Found trainer by username '{}' and password '{}'", username, password);
        } else {
            log.debug("Trainer with username '{}' and password '{}' not found.", username, password);
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

    @Transactional(readOnly = true)
    @Override
    public Optional<Trainer> getByFirstNameAndLastName(String firstName, String lastName) {
        Optional<Trainer> foundTrainer = entityManager
                .createQuery("from Trainer t where t.firstName = :firstName and t.lastName = :lastName", Trainer.class)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .getResultStream().findFirst();
        if (foundTrainer.isPresent()) {
            log.debug("Found trainer by first name '{}' and last name '{}': {}", firstName, lastName, foundTrainer.get());
        } else {
            log.debug("Trainer with first name '{}' and last name '{}' not found.", firstName, lastName);
        }
        return foundTrainer;
    }
}
