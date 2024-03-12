package com.epam.gymcrm.repository;

import com.epam.gymcrm.dao.impl.TraineeDaoCollection;
import com.epam.gymcrm.model.Trainee;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class TraineeDaoCollectionTest {
    @InjectMocks
    private TraineeDaoCollection traineeDaoCollection;

    @Test
    void testSave() throws Exception {
        // Given
        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Doe");

        // When
        Trainee savedTrainee = traineeDaoCollection.save(trainee);

        // Then
        assertEquals("John.Doe", savedTrainee.getUsername());
    }

    @Test
    void testFindAll() {
        // When
        List<Trainee> allTrainees = traineeDaoCollection.findAll();

        // Then
        assertEquals(0, allTrainees.size());
    }

    @Test
    void testFindById() {
        // Given
        Trainee trainee = new Trainee();
        trainee.setId(1L);
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        traineeDaoCollection.save(trainee);

        // When
        Optional<Trainee> foundTrainee = traineeDaoCollection.findById(1L);

        // Then
        assertEquals(trainee, foundTrainee.orElse(null));
    }

    @Test
    void testFindById_NonExistentTrainee() {
        // When
        Optional<Trainee> trainee = traineeDaoCollection.findById(99L);

        // Then
        assertTrue(trainee.isEmpty());
    }

    @Test
    void testUpdate() {
        // Given
        Trainee trainee = new Trainee();
        trainee.setId(1L);
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        traineeDaoCollection.save(trainee);

        Trainee updatedTrainee = new Trainee();
        updatedTrainee.setId(1L);
        updatedTrainee.setFirstName("Jane");
        updatedTrainee.setLastName("Smith");

        // When
        Trainee result = traineeDaoCollection.update(updatedTrainee);

        // Then
        assertEquals(updatedTrainee, result);
    }

    @Test
    void testUpdate_NonExistentTrainee() {
        // Given
        Trainee nonExistentTrainee = new Trainee();
        nonExistentTrainee.setId(99L);
        nonExistentTrainee.setFirstName("John");
        nonExistentTrainee.setLastName("Doe");

        // When
        Trainee updatedTrainee = traineeDaoCollection.update(nonExistentTrainee);

        // Then
        assertNull(updatedTrainee);
    }

    @Test
    void testDelete() {
        // Given
        Trainee trainee = new Trainee();
        trainee.setId(1L);
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        traineeDaoCollection.save(trainee);

        // When
        traineeDaoCollection.delete(1L);

        // Then
        assertEquals(0, traineeDaoCollection.findAll().size());
    }

    @Test
    void testFindByFirstNameAndLastName() {
        // Given
        Trainee trainee = new Trainee();
        trainee.setId(1L);
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        traineeDaoCollection.save(trainee);

        // When
        Optional<Trainee> foundTrainee = traineeDaoCollection.findByFirstNameAndLastName("John", "Doe");

        // Then
        assertEquals(trainee, foundTrainee.orElse(null));
    }

    @Test
    void testFindByFirstNameAndLastName_NonExistentTrainee() {
        // When
        Optional<Trainee> trainee = traineeDaoCollection.findByFirstNameAndLastName("Jane", "Smith");

        // Then
        assertTrue(trainee.isEmpty());
    }
}
