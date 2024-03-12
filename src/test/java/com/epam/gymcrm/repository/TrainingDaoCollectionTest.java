package com.epam.gymcrm.repository;

import com.epam.gymcrm.dao.impl.TrainerDaoCollection;
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
    void testSave() {
        // Given
        Training trainingToSave = new Training();
        trainingToSave.setTrainingName("Running");

        // When
        Training savedTraining = trainingDaoCollection.save(trainingToSave);

        // Then
        assertNotNull(savedTraining.getId());
        assertEquals("Running", savedTraining.getTrainingName());
    }

    @Test
    void testFindAll() {
        // Given
        Training training1 = new Training();
        training1.setTrainingName("Running");
        Training training2 = new Training();
        training2.setTrainingName("Swimming");
        trainingDaoCollection.save(training1);
        trainingDaoCollection.save(training2);

        // When
        List<Training> allTrainings = trainingDaoCollection.findAll();

        // Then
        assertEquals(2, allTrainings.size());
        assertTrue(allTrainings.stream().anyMatch(t -> t.getTrainingName().equals("Running")));
        assertTrue(allTrainings.stream().anyMatch(t -> t.getTrainingName().equals("Swimming")));
    }

    @Test
    void testFindById_ExistentTraining() {
        // Given
        Training existingTraining = new Training();
        existingTraining.setId(1L);
        existingTraining.setTrainingName("Running");
        trainingDaoCollection.save(existingTraining);

        // When
        Optional<Training> foundTraining = trainingDaoCollection.findById(1L);

        // Then
        assertTrue(foundTraining.isPresent());
        assertEquals("Running", foundTraining.get().getTrainingName());
    }

    @Test
    void testFindById_TrainingNotFound() {
        // When
        Optional<Training> trainingOptional = trainingDaoCollection.findById(999L);

        // Then
        assertTrue(trainingOptional.isEmpty());
    }
}
