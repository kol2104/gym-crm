package com.epam.gymcrm.repository;

import com.epam.gymcrm.dao.impl.TrainerDaoCollection;
import com.epam.gymcrm.model.Trainer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class TrainerDaoCollectionTest {

    @InjectMocks
    private TrainerDaoCollection trainerDaoCollection;

    @Test
    void testCreate() {

        // Given
        Trainer trainer = new Trainer();
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainer.setUsername("John.Doe");
        trainer.setPassword("password");
        trainer.setActive(true);

        // When
        Trainer createdTrainer = trainerDaoCollection.create(trainer);

        // Then
        assertNotNull(createdTrainer.getId());
        assertEquals(trainer.getUsername(), createdTrainer.getUsername());
        assertEquals(trainer.getFirstName(), createdTrainer.getFirstName());
        assertEquals(trainer.getLastName(), createdTrainer.getLastName());
        assertEquals(trainer.getPassword(), createdTrainer.getPassword());
        assertTrue(createdTrainer.isActive());
    }

    @Test
    void testUpdate_NonExistentTrainer() {
        // Given
        Trainer nonExistentTrainer = new Trainer();
        nonExistentTrainer.setId(99L);
        nonExistentTrainer.setFirstName("John");
        nonExistentTrainer.setLastName("Doe");

        // When
        Trainer updatedTrainer = trainerDaoCollection.update(nonExistentTrainer);

        // Then
        assertNull(updatedTrainer);
    }

    @Test
    void testGetById_NonExistentTrainer() {
        // When
        Optional<Trainer> trainer = trainerDaoCollection.getById(99L);

        // Then
        assertTrue(trainer.isEmpty());
    }

    @Test
    void testGetByFirstNameAndLastName_NonExistentTrainer() {
        // When
        Optional<Trainer> trainer = trainerDaoCollection.getByFirstNameAndLastName("Jane", "Smith");

        // Then
        assertTrue(trainer.isEmpty());
    }

    @Test
    void testUpdate_ExistentTrainer() {
        // Given
        Trainer existingTrainer = new Trainer();
        existingTrainer.setId(1L);
        existingTrainer.setFirstName("John");
        existingTrainer.setLastName("Doe");
        trainerDaoCollection.create(existingTrainer);

        // When
        existingTrainer.setFirstName("Jane");
        Trainer updatedTrainer = trainerDaoCollection.update(existingTrainer);

        // Then
        assertNotNull(updatedTrainer);
        assertEquals("Jane", updatedTrainer.getFirstName());
    }

    @Test
    void testGetById_ExistentTrainer() {
        // Given
        Trainer existingTrainer = new Trainer();
        existingTrainer.setId(1L);
        existingTrainer.setFirstName("John");
        existingTrainer.setLastName("Doe");
        trainerDaoCollection.create(existingTrainer);

        // When
        Optional<Trainer> foundTrainer = trainerDaoCollection.getById(1L);

        // Then
        assertTrue(foundTrainer.isPresent());
        assertEquals("John", foundTrainer.get().getFirstName());
    }

    @Test
    void testGetByFirstNameAndLastName_ExistentTrainer() {
        // Given
        Trainer existingTrainer = new Trainer();
        existingTrainer.setId(1L);
        existingTrainer.setFirstName("John");
        existingTrainer.setLastName("Doe");
        trainerDaoCollection.create(existingTrainer);

        // When
        Optional<Trainer> foundTrainer = trainerDaoCollection.getByFirstNameAndLastName("John", "Doe");

        // Then
        assertTrue(foundTrainer.isPresent());
        assertEquals("John", foundTrainer.get().getFirstName());
        assertEquals("Doe", foundTrainer.get().getLastName());
    }
}
