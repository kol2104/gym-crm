package com.epam.gymcrm.repository;

import com.epam.gymcrm.dao.impl.TraineeDaoCollection;
import com.epam.gymcrm.model.Trainee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class TraineeDaoCollectionTest {
    @InjectMocks
    private TraineeDaoCollection traineeDaoCollection;

    @Test
    void testCreate() throws Exception {
        // Given
        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        trainee.setUsername("John.Doe");
        trainee.setPassword("password");
        trainee.setActive(true);

        // When
        Trainee createdTrainee = traineeDaoCollection.create(trainee);

        // Then
        assertNotNull(createdTrainee.getId());
        assertEquals(trainee.getUsername(), createdTrainee.getUsername());
        assertEquals(trainee.getFirstName(), createdTrainee.getFirstName());
        assertEquals(trainee.getLastName(), createdTrainee.getLastName());
        assertEquals(trainee.getPassword(), createdTrainee.getPassword());
        assertTrue(createdTrainee.isActive());
    }

    @Test
    void testGetAll() {
        // When
        List<Trainee> allTrainees = traineeDaoCollection.getAll();

        // Then
        assertEquals(0, allTrainees.size());
    }

    @Test
    void testGetById() {
        // Given
        Trainee trainee = new Trainee();
        trainee.setId(1L);
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        traineeDaoCollection.create(trainee);

        // When
        Optional<Trainee> foundTrainee = traineeDaoCollection.getById(1L);

        // Then
        assertEquals(trainee, foundTrainee.orElse(null));
    }

    @Test
    void testGetById_NonExistentTrainee() {
        // When
        Optional<Trainee> trainee = traineeDaoCollection.getById(99L);

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
        traineeDaoCollection.create(trainee);

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
        traineeDaoCollection.create(trainee);

        // When
        traineeDaoCollection.delete(1L);

        // Then
        assertEquals(0, traineeDaoCollection.getAll().size());
    }

    @Test
    void testGetByFirstNameAndLastName() {
        // Given
        Trainee trainee = new Trainee();
        trainee.setId(1L);
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        traineeDaoCollection.create(trainee);

        // When
        Optional<Trainee> foundTrainee = traineeDaoCollection.getByFirstNameAndLastName("John", "Doe");

        // Then
        assertEquals(trainee, foundTrainee.orElse(null));
    }

    @Test
    void testGetByFirstNameAndLastName_NonExistentTrainee() {
        // When
        Optional<Trainee> trainee = traineeDaoCollection.getByFirstNameAndLastName("Jane", "Smith");

        // Then
        assertTrue(trainee.isEmpty());
    }
}
