package com.epam.gymcrm.repository;

import com.epam.gymcrm.dao.impl.TrainingDaoCollection;
import com.epam.gymcrm.model.Training;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class TrainingDaoCollectionTest {

    @InjectMocks
    private TrainingDaoCollection trainingDaoCollection;

    @Test
    void testCreate() {
        // Given
        Training trainingToSave = new Training();
        trainingToSave.setTrainingName("Running");

        // When
        Training createdTraining = trainingDaoCollection.create(trainingToSave);

        // Then
        assertNotNull(createdTraining.getId());
        assertEquals("Running", createdTraining.getTrainingName());
    }

    @Test
    void testGetAll() {
        // Given
        Training training1 = new Training();
        training1.setTrainingName("Running");
        Training training2 = new Training();
        training2.setTrainingName("Swimming");
        trainingDaoCollection.create(training1);
        trainingDaoCollection.create(training2);

        // When
        List<Training> allTrainings = trainingDaoCollection.getAll();

        // Then
        assertEquals(2, allTrainings.size());
        assertTrue(allTrainings.stream().anyMatch(t -> t.getTrainingName().equals("Running")));
        assertTrue(allTrainings.stream().anyMatch(t -> t.getTrainingName().equals("Swimming")));
    }

    @Test
    void testGetById_ExistentTraining() {
        // Given
        Training existingTraining = new Training();
        existingTraining.setId(1L);
        existingTraining.setTrainingName("Running");
        trainingDaoCollection.create(existingTraining);

        // When
        Optional<Training> foundTraining = trainingDaoCollection.getById(1L);

        // Then
        assertTrue(foundTraining.isPresent());
        assertEquals("Running", foundTraining.get().getTrainingName());
    }

    @Test
    void testGetById_TrainingNotFound() {
        // When
        Optional<Training> trainingOptional = trainingDaoCollection.getById(999L);

        // Then
        assertTrue(trainingOptional.isEmpty());
    }
}
