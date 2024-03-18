package com.epam.gymcrm.dao.impl;

import com.epam.gymcrm.config.property.YamlPropertySourceFactory;
import com.epam.gymcrm.dao.TrainingDao;
import com.epam.gymcrm.model.Training;
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
public class TrainingDaoCollection implements TrainingDao {

    private final Map<Long, Training> trainings = new HashMap<>();
    private long nextId = 1;

    @Value("${data.json.path.trainings}")
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

            List<Training> preparedData = objectMapper.readValue(jsonFile, new TypeReference<List<Training>>() {});
            preparedData.forEach(this::create);
            log.info("Initialization of TrainingDaoCollection completed successfully.");
        } catch (IOException e) {
            log.error("Error occurred during initialization of TrainingDaoCollection: {}", e.getMessage());
        }
    }
    @Override
    public Training create(Training training) {
        training.setId(nextId++);
        trainings.put(training.getId(), training);
        log.info("Training created successfully: {}", training);
        return training;
    }

    @Override
    public List<Training> getAll() {
        List<Training> allTrainings = new ArrayList<>(trainings.values());
        log.debug("Found {} trainings.", allTrainings.size());
        return allTrainings;
    }

    @Override
    public Optional<Training> getById(Long id) {
        Training training = trainings.get(id);
        if (training != null) {
            log.debug("Found training by id {}: {}", id, training);
        } else {
            log.debug("Training with id {} not found.", id);
        }
        return Optional.ofNullable(training);
    }
}
