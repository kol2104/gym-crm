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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class TrainerDaoCollectionTest {

    @InjectMocks
    private TrainerDaoCollection trainerDaoCollection;

    @Test
    void testSave_WithExistingTrainer() throws IOException {
        // Given
        Trainer existingTrainer = new Trainer();
        existingTrainer.setId(1L);
        existingTrainer.setFirstName("John");
        existingTrainer.setLastName("Doe");
        trainerDaoCollection.save(existingTrainer);

        // When
        Trainer trainer = new Trainer();
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        Trainer savedTrainer = trainerDaoCollection.save(trainer);

        // Then
        assertNotEquals("John.Doe", savedTrainer.getUsername());
        assertTrue(savedTrainer.getUsername().startsWith("John.Doe"));
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
    void testFindById_NonExistentTrainer() {
        // When
        Optional<Trainer> trainer = trainerDaoCollection.findById(99L);

        // Then
        assertTrue(trainer.isEmpty());
    }

    @Test
    void testFindByFirstNameAndLastName_NonExistentTrainer() {
        // When
        Optional<Trainer> trainer = trainerDaoCollection.findByFirstNameAndLastName("Jane", "Smith");

        // Then
        assertTrue(trainer.isEmpty());
    }

    @Test
    void testUpdate_ExistentTrainer() throws IOException {
        // Given
        Trainer existingTrainer = new Trainer();
        existingTrainer.setId(1L);
        existingTrainer.setFirstName("John");
        existingTrainer.setLastName("Doe");
        trainerDaoCollection.save(existingTrainer);

        // When
        existingTrainer.setFirstName("Jane");
        Trainer updatedTrainer = trainerDaoCollection.update(existingTrainer);

        // Then
        assertNotNull(updatedTrainer);
        assertEquals("Jane", updatedTrainer.getFirstName());
    }

    @Test
    void testFindById_ExistentTrainer() {
        // Given
        Trainer existingTrainer = new Trainer();
        existingTrainer.setId(1L);
        existingTrainer.setFirstName("John");
        existingTrainer.setLastName("Doe");
        trainerDaoCollection.save(existingTrainer);

        // When
        Optional<Trainer> foundTrainer = trainerDaoCollection.findById(1L);

        // Then
        assertTrue(foundTrainer.isPresent());
        assertEquals("John", foundTrainer.get().getFirstName());
    }

    @Test
    void testFindByFirstNameAndLastName_ExistentTrainer() {
        // Given
        Trainer existingTrainer = new Trainer();
        existingTrainer.setId(1L);
        existingTrainer.setFirstName("John");
        existingTrainer.setLastName("Doe");
        trainerDaoCollection.save(existingTrainer);

        // When
        Optional<Trainer> foundTrainer = trainerDaoCollection.findByFirstNameAndLastName("John", "Doe");

        // Then
        assertTrue(foundTrainer.isPresent());
        assertEquals("John", foundTrainer.get().getFirstName());
        assertEquals("Doe", foundTrainer.get().getLastName());
    }
}
