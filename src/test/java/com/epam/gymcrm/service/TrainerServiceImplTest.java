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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
}
