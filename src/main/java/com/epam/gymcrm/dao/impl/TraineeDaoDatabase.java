package com.epam.gymcrm.dao.impl;

import com.epam.gymcrm.config.property.YamlPropertySourceFactory;
import com.epam.gymcrm.dao.TraineeDao;
import com.epam.gymcrm.model.Trainee;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
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
public class TraineeDaoDatabase implements TraineeDao {
    @Value("${data.json.path.trainees}")
    private String dataJsonFilePath;

    private final ObjectMapper objectMapper;
    private final EntityManager entityManager;

    @PostConstruct
    private void init() {
        try {
            File jsonFile = ResourceUtils.getFile("classpath:" + dataJsonFilePath);

            List<Trainee> preparedData = objectMapper.readValue(jsonFile, new TypeReference<>() {});
            preparedData.forEach(this::create);
            log.info("Initialization of TraineeDaoDatabase completed successfully.");
        } catch (IOException e) {
            log.error("Error occurred during initialization of TraineeDaoDatabase: {}", e.getMessage());
        }
    }

    @Transactional
    @Override
    public Trainee create(Trainee trainee) {
        entityManager.persist(trainee);
        log.info("Trainee created successfully: {}", trainee);
        return trainee;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Trainee> getAll() {
        List<Trainee> allTrainees = entityManager.createQuery("from Trainee", Trainee.class).getResultList();
        log.debug("Found {} trainees.", allTrainees.size());
        return allTrainees;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Trainee> getById(Long id) {
        Trainee trainee = entityManager.find(Trainee.class, id);
        if (trainee != null) {
            log.debug("Found trainee by id {}: {}", id, trainee);
        } else {
            log.debug("Trainee with id {} not found.", id);
        }
        return Optional.ofNullable(trainee);
    }

    @Transactional
    @Override
    public Trainee update(Trainee trainee) {
        if (entityManager.find(Trainee.class, trainee.getId()) == null) {
            log.debug("Trainee with id {} not found. Unable to update.", trainee.getId());
            return null;
        }
        entityManager.merge(trainee);
        log.info("Trainee updated successfully: {}", trainee);
        return trainee;
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Query query = entityManager.createQuery("delete from Trainee t where t.id = :id");
        int deletedRows = query.executeUpdate();
        if (deletedRows != 0) {
            log.info("Trainee with id '{}' deleted successfully", id);
        } else {
            log.debug("Trainee with id {} not found. Unable to delete.", id);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Trainee> getByFirstNameAndLastName(String firstName, String lastName) {
        Optional<Trainee> foundTrainee = Optional.ofNullable(entityManager
                .createQuery("from Trainee t where t.firstName = :firstName and t.lastName = :lastName", Trainee.class)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .getSingleResult());
        if (foundTrainee.isPresent()) {
            log.debug("Found trainee by first name '{}' and last name '{}': {}", firstName, lastName, foundTrainee.get());
        } else {
            log.debug("Trainee with first name '{}' and last name '{}' not found.", firstName, lastName);
        }
        return foundTrainee;
    }
}
