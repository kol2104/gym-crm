package com.epam.gymcrm.dao.impl;

import com.epam.gymcrm.config.property.YamlPropertySourceFactory;
import com.epam.gymcrm.dao.TrainerDao;
import com.epam.gymcrm.model.Trainer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
public class TrainerDaoCollection implements TrainerDao {
    private final Map<Long, Trainer> trainers = new HashMap<>();
    private long nextId = 1;

    @Value("${data.json.path.trainers}")
    private String dataJsonFilePath;

    private ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    private void init() {
        try {
            File jsonFile = ResourceUtils.getFile("classpath:" + dataJsonFilePath);
            List<Trainer> preparedData = objectMapper.readValue(jsonFile, new TypeReference<List<Trainer>>() {});
            preparedData.forEach(this::create);
            log.info("Initialization of TrainerDaoCollection completed successfully.");
        } catch (IOException e) {
            log.error("Error occurred during initialization of TrainerDaoCollection: {}", e.getMessage());
        }
    }

    @Override
    public Trainer create(Trainer trainer) {
        trainer.setId(nextId++);
        trainers.put(trainer.getId(), trainer);
        log.info("Trainer created successfully: {}", trainer);
        return trainer;
    }

    @Override
    public List<Trainer> getAll() {
        List<Trainer> allTrainers = new ArrayList<>(trainers.values());
        log.debug("Found {} trainers.", allTrainers.size());
        return allTrainers;
    }

    @Override
    public Optional<Trainer> getById(Long id) {
        Trainer trainer = trainers.get(id);
        if (trainer != null) {
            log.debug("Found trainer by id {}: {}", id, trainer);
        } else {
            log.debug("Trainer with id {} not found.", id);
        }
        return Optional.ofNullable(trainer);
    }

    @Override
    public Trainer update(Trainer trainer) {
        if (trainers.containsKey(trainer.getId())) {
            trainers.put(trainer.getId(), trainer);
            log.info("Trainer updated successfully: {}", trainer);
            return trainer;
        } else {
            log.debug("Trainer with id {} not found. Unable to update.", trainer.getId());
            return null;
        }
    }

    @Override
    public Optional<Trainer> getByFirstNameAndLastName(String firstName, String lastName) {
        Optional<Trainer> foundTrainer = trainers.values().stream()
                .filter(trainer -> trainer.getFirstName().equals(firstName) && trainer.getLastName().equals(lastName))
                .findFirst();
        if (foundTrainer.isPresent()) {
            log.debug("Found trainer by first name '{}' and last name '{}': {}", firstName, lastName, foundTrainer.get());
        } else {
            log.debug("Trainer with first name '{}' and last name '{}' not found.", firstName, lastName);
        }
        return foundTrainer;
    }
}
