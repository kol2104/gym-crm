package com.epam.gymcrm.dao.impl;

import com.epam.gymcrm.config.property.YamlPropertySourceFactory;
import com.epam.gymcrm.dao.TrainingDao;
import com.epam.gymcrm.model.Training;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
public class TrainingDaoDatabase implements TrainingDao {
    @Value("${data.json.path.trainings}")
    private String dataJsonFilePath;

    private final ObjectMapper objectMapper;
    private final EntityManager entityManager;

    @PostConstruct
    private void init() {
        try {
            File jsonFile = ResourceUtils.getFile("classpath:" + dataJsonFilePath);

            List<Training> preparedData = objectMapper.readValue(jsonFile, new TypeReference<>() {});
            preparedData.forEach(this::create);
            log.info("Initialization of TrainingDaoDatabase completed successfully.");
        } catch (IOException e) {
            log.error("Error occurred during initialization of TrainingDaoDatabase: {}", e.getMessage());
        }
    }
    @Override
    public Training create(Training training) {
        entityManager.persist(training);
        log.info("Training created successfully: {}", training);
        return training;
    }

    @Override
    public List<Training> getAll() {
        List<Training> allTrainings = entityManager.createQuery("from Training", Training.class).getResultList();
        log.debug("Found {} trainings.", allTrainings.size());
        return allTrainings;
    }

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
}
