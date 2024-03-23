package com.epam.gymcrm.dao.impl;

import com.epam.gymcrm.config.property.YamlPropertySourceFactory;
import com.epam.gymcrm.dao.TrainerDao;
import com.epam.gymcrm.model.Trainer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
public class TrainerDaoDatabase implements TrainerDao {

    @Value("${data.json.path.trainers}")
    private String dataJsonFilePath;

    private final ObjectMapper objectMapper;
    private final EntityManager entityManager;

    @PostConstruct
    private void init() {
        try {
            File jsonFile = ResourceUtils.getFile("classpath:" + dataJsonFilePath);
            List<Trainer> preparedData = objectMapper.readValue(jsonFile, new TypeReference<>() {});
            preparedData.forEach(this::create);
            log.info("Initialization of TrainerDaoDatabase completed successfully.");
        } catch (IOException e) {
            log.error("Error occurred during initialization of TrainerDaoDatabase: {}", e.getMessage());
        }
    }

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

    @Transactional
    @Override
    public Trainer update(Trainer trainer) {
        if (entityManager.find(Trainer.class, trainer.getId()) == null) {
            log.debug("Trainer with id {} not found. Unable to update.", trainer.getId());
            return null;
        }
        entityManager.merge(trainer);
        log.info("Trainee updated successfully: {}", trainer);
        return trainer;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Trainer> getByFirstNameAndLastName(String firstName, String lastName) {
        Optional<Trainer> foundTrainer = Optional.ofNullable(entityManager
                .createQuery("from Trainer t where t.firstName = :firstName and t.lastName = :lastName", Trainer.class)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .getSingleResult());
        if (foundTrainer.isPresent()) {
            log.debug("Found trainer by first name '{}' and last name '{}': {}", firstName, lastName, foundTrainer.get());
        } else {
            log.debug("Trainer with first name '{}' and last name '{}' not found.", firstName, lastName);
        }
        return foundTrainer;
    }
}
