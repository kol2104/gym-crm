package com.epam.gymcrm.service;

import com.epam.gymcrm.dao.TrainerDao;
import com.epam.gymcrm.exception.TrainerNotFoundException;
import com.epam.gymcrm.model.Trainer;
import com.epam.gymcrm.service.impl.TrainerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

    @Mock
    private TrainerDao trainerDao;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @Test
    void testCreateTrainer() {
        // Given
        Trainer trainer = new Trainer();
        when(trainerDao.create(trainer)).thenReturn(trainer);

        // When
        Trainer createdTrainer = trainerService.create(trainer);

        // Then
        assertNotNull(createdTrainer);
        assertEquals(trainer, createdTrainer);
        verify(trainerDao, times(1)).create(trainer);
    }

    @Test
    void testGetAllTrainers() {
        // Given
        List<Trainer> trainerList = new ArrayList<>();
        when(trainerDao.getAll()).thenReturn(trainerList);

        // When
        List<Trainer> foundTrainers = trainerService.getAll();

        // Then
        assertNotNull(foundTrainers);
        assertEquals(trainerList, foundTrainers);
        verify(trainerDao, times(1)).getAll();
    }

    @Test
    void testGetTrainerById() {
        // Given
        long trainerId = 1L;
        Trainer trainer = new Trainer();
        when(trainerDao.getById(trainerId)).thenReturn(Optional.of(trainer));

        // When
        Trainer foundTrainer = trainerService.getById(trainerId);

        // Then
        assertNotNull(foundTrainer);
        assertEquals(trainer, foundTrainer);
        verify(trainerDao, times(1)).getById(trainerId);
    }

    @Test
    void testGetTrainerByIdNotFound() {
        // Given
        long trainerId = 1L;
        when(trainerDao.getById(trainerId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(TrainerNotFoundException.class, () -> trainerService.getById(trainerId));
        verify(trainerDao, times(1)).getById(trainerId);
    }

    @Test
    void testGetTrainerByUsername() {
        String username = "john.doe";
        Trainer trainer = new Trainer();
        trainer.setUsername(username);

        when(trainerDao.getByUsername(username)).thenReturn(Optional.of(trainer));

        Trainer foundTrainer = trainerService.getByUsername(username);

        assertNotNull(foundTrainer);
        assertEquals(username, foundTrainer.getUsername());
        verify(trainerDao, times(1)).getByUsername(username);
    }

    @Test
    void testGetTrainerByNonExistingUsername() {
        String nonExistingUsername = "non_existing_username";

        when(trainerDao.getByUsername(nonExistingUsername)).thenReturn(Optional.empty());

        TrainerNotFoundException exception = assertThrows(TrainerNotFoundException.class, () -> {
            trainerService.getByUsername(nonExistingUsername);
        });

        assertEquals("Trainer with username '" + nonExistingUsername + "' not found", exception.getMessage());
        verify(trainerDao, times(1)).getByUsername(nonExistingUsername);
    }

    @Test
    void testIsUsernameAndPasswordValidWithValidCredentials() {
        String username = "john.doe";
        String password = "password";
        Trainer trainer = new Trainer();
        trainer.setUsername(username);
        trainer.setPassword(password);

        when(trainerDao.getByUsernameAndPassword(username, password)).thenReturn(Optional.of(trainer));

        boolean isValid = trainerService.isUsernameAndPasswordValid(username, password);

        assertTrue(isValid);
        verify(trainerDao, times(1)).getByUsernameAndPassword(username, password);
    }

    @Test
    void testIsUsernameAndPasswordValidWithInvalidCredentials() {
        String username = "john.doe";
        String password = "password";

        when(trainerDao.getByUsernameAndPassword(username, password)).thenReturn(Optional.empty());

        boolean isValid = trainerService.isUsernameAndPasswordValid(username, password);

        assertFalse(isValid);
        verify(trainerDao, times(1)).getByUsernameAndPassword(username, password);
    }

    @Test
    void testUpdateTrainer() {
        // Given
        long trainerId = 1L;
        Trainer trainer = new Trainer();
        when(trainerDao.getById(trainerId)).thenReturn(Optional.of(trainer));
        when(trainerDao.update(trainer)).thenReturn(trainer);

        // When
        Trainer updatedTrainer = trainerService.update(trainerId, trainer);

        // Then
        assertNotNull(updatedTrainer);
        assertEquals(trainer, updatedTrainer);
        verify(trainerDao, times(1)).getById(trainerId);
        verify(trainerDao, times(1)).update(trainer);
    }

    @Test
    void testUpdateTrainerNotFound() {
        // Given
        long trainerId = 1L;
        Trainer trainer = new Trainer();
        when(trainerDao.getById(trainerId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(TrainerNotFoundException.class, () -> trainerService.update(trainerId, trainer));
        verify(trainerDao, times(1)).getById(trainerId);
        verify(trainerDao, never()).update(trainer);
    }

    @Test
    void testUpdatePasswordWithNonNullPassword() {
        Long id = 1L;
        String newPassword = "newPassword";
        Trainer trainer = new Trainer();
        trainer.setId(id);

        when(trainerDao.getById(id)).thenReturn(Optional.of(trainer));

        assertDoesNotThrow(() -> trainerService.updatePassword(id, newPassword));
        assertEquals(newPassword, trainer.getPassword());
        verify(trainerDao, times(1)).getById(id);
        verify(trainerDao, times(1)).update(trainer);
    }

    @Test
    void testUpdatePasswordWithNullPassword() {
        Long id = 1L;
        Trainer trainer = new Trainer();
        trainer.setId(id);

        when(trainerDao.getById(id)).thenReturn(Optional.of(trainer));

        assertDoesNotThrow(() -> trainerService.updatePassword(id, null));
        assertNull(trainer.getPassword());
        verify(trainerDao, times(1)).getById(id);
        verify(trainerDao, times(1)).update(trainer);
    }

    @Test
    void testUpdatePasswordWithNonExistingTrainer() {
        Long id = 1L;

        when(trainerDao.getById(id)).thenReturn(Optional.empty());

        assertThrows(TrainerNotFoundException.class, () -> trainerService.updatePassword(id, "newPassword"));
        verify(trainerDao, times(1)).getById(id);
        verify(trainerDao, never()).update(any());
    }

    @Test
    void testActivateExistingTrainer() {
        Long id = 1L;
        Trainer trainer = new Trainer();
        trainer.setId(id);

        when(trainerDao.getById(id)).thenReturn(Optional.of(trainer));

        assertDoesNotThrow(() -> trainerService.activate(id));
        assertTrue(trainer.isActive());
        verify(trainerDao, times(1)).getById(id);
        verify(trainerDao, times(1)).update(trainer);
    }

    @Test
    void testActivateNonExistingTrainer() {
        Long id = 1L;

        when(trainerDao.getById(id)).thenReturn(Optional.empty());

        assertThrows(TrainerNotFoundException.class, () -> trainerService.activate(id));
        verify(trainerDao, times(1)).getById(id);
        verify(trainerDao, never()).update(any());
    }

    @Test
    void testActivateAlreadyActivatedTrainer() {
        Long id = 1L;
        Trainer trainer = new Trainer();
        trainer.setId(id);
        trainer.setActive(true);

        when(trainerDao.getById(id)).thenReturn(Optional.of(trainer));

        assertDoesNotThrow(() -> trainerService.activate(id));
        assertTrue(trainer.isActive()); // Ensure active status remains true
        verify(trainerDao, times(1)).getById(id);
        verify(trainerDao, never()).update(any());
    }

    @Test
    void testDeactivateExistingTrainer() {
        Long id = 1L;
        Trainer trainer = new Trainer();
        trainer.setId(id);
        trainer.setActive(true);

        when(trainerDao.getById(id)).thenReturn(Optional.of(trainer));

        assertDoesNotThrow(() -> trainerService.deactivate(id));
        assertFalse(trainer.isActive());
        verify(trainerDao, times(1)).getById(id);
        verify(trainerDao, times(1)).update(trainer);
    }

    @Test
    void testDeactivateNonExistingTrainer() {
        Long id = 1L;

        when(trainerDao.getById(id)).thenReturn(Optional.empty());

        assertThrows(TrainerNotFoundException.class, () -> trainerService.deactivate(id));
        verify(trainerDao, times(1)).getById(id);
        verify(trainerDao, never()).update(any());
    }

    @Test
    void testDeactivateAlreadyDeactivatedTrainer() {
        Long id = 1L;
        Trainer trainer = new Trainer();
        trainer.setId(id);
        trainer.setActive(false);

        when(trainerDao.getById(id)).thenReturn(Optional.of(trainer));

        assertDoesNotThrow(() -> trainerService.deactivate(id));
        assertFalse(trainer.isActive()); // Ensure active status remains false
        verify(trainerDao, times(1)).getById(id);
        verify(trainerDao, never()).update(any());
    }
}
