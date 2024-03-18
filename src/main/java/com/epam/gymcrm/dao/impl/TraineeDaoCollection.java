package com.epam.gymcrm.dao.impl;

import com.epam.gymcrm.config.property.YamlPropertySourceFactory;
import com.epam.gymcrm.dao.TraineeDao;
import com.epam.gymcrm.model.Trainee;
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
public class TraineeDaoCollection implements TraineeDao {

    private final Map<Long, Trainee> trainees = new HashMap<>();
    private long nextId = 1;

    @Value("${data.json.path.trainees}")
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

            List<Trainee> preparedData = objectMapper.readValue(jsonFile, new TypeReference<List<Trainee>>() {});
            preparedData.forEach(this::create);
            log.info("Initialization of TraineeDaoCollection completed successfully.");
        } catch (IOException e) {
            log.error("Error occurred during initialization of TraineeDaoCollection: {}", e.getMessage());
        }
    }
    @Override
    public Trainee create(Trainee trainee) {
        trainee.setId(nextId++);
        trainees.put(trainee.getId(), trainee);
        log.info("Trainee created successfully: {}", trainee);
        return trainee;
    }

    @Override
    public List<Trainee> getAll() {
        List<Trainee> allTrainees = new ArrayList<>(trainees.values());
        log.debug("Found {} trainees.", allTrainees.size());
        return allTrainees;
    }

    @Override
    public Optional<Trainee> getById(Long id) {
        Trainee trainee = trainees.get(id);
        if (trainee != null) {
            log.debug("Found trainee by id {}: {}", id, trainee);
        } else {
            log.debug("Trainee with id {} not found.", id);
        }
        return Optional.ofNullable(trainee);
    }

    @Override
    public Trainee update(Trainee trainee) {
        if (trainees.containsKey(trainee.getId())) {
            trainees.put(trainee.getId(), trainee);
            log.info("Trainee updated successfully: {}", trainee);
            return trainee;
        } else {
            log.debug("Trainee with id {} not found. Unable to update.", trainee.getId());
            return null;
        }
    }

    @Override
    public void delete(Long id) {
        Trainee removedTrainee = trainees.remove(id);
        if (removedTrainee != null) {
            log.info("Trainee deleted successfully: {}", removedTrainee);
        } else {
            log.debug("Trainee with id {} not found. Unable to delete.", id);
        }
    }

    @Override
    public Optional<Trainee> getByFirstNameAndLastName(String firstName, String lastName) {
        Optional<Trainee> foundTrainee = trainees.values().stream()
                .filter(trainee -> trainee.getFirstName().equals(firstName) && trainee.getLastName().equals(lastName))
                .findFirst();
        if (foundTrainee.isPresent()) {
            log.debug("Found trainee by first name '{}' and last name '{}': {}", firstName, lastName, foundTrainee.get());
        } else {
            log.debug("Trainee with first name '{}' and last name '{}' not found.", firstName, lastName);
        }
        return foundTrainee;
    }
}
