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
    void testSaveTrainer() {
        // Given
        Trainer trainer = new Trainer();
        when(trainerDao.save(trainer)).thenReturn(trainer);

        // When
        Trainer savedTrainer = trainerService.save(trainer);

        // Then
        assertNotNull(savedTrainer);
        assertEquals(trainer, savedTrainer);
        verify(trainerDao, times(1)).save(trainer);
    }

    @Test
    void testFindAllTrainers() {
        // Given
        List<Trainer> trainerList = new ArrayList<>();
        when(trainerDao.findAll()).thenReturn(trainerList);

        // When
        List<Trainer> foundTrainers = trainerService.findAll();

        // Then
        assertNotNull(foundTrainers);
        assertEquals(trainerList, foundTrainers);
        verify(trainerDao, times(1)).findAll();
    }

    @Test
    void testFindTrainerById() {
        // Given
        long trainerId = 1L;
        Trainer trainer = new Trainer();
        when(trainerDao.findById(trainerId)).thenReturn(Optional.of(trainer));

        // When
        Trainer foundTrainer = trainerService.findById(trainerId);

        // Then
        assertNotNull(foundTrainer);
        assertEquals(trainer, foundTrainer);
        verify(trainerDao, times(1)).findById(trainerId);
    }

    @Test
    void testFindTrainerByIdNotFound() {
        // Given
        long trainerId = 1L;
        when(trainerDao.findById(trainerId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(TrainerNotFoundException.class, () -> trainerService.findById(trainerId));
        verify(trainerDao, times(1)).findById(trainerId);
    }

    @Test
    void testUpdateTrainer() {
        // Given
        long trainerId = 1L;
        Trainer trainer = new Trainer();
        when(trainerDao.findById(trainerId)).thenReturn(Optional.of(trainer));
        when(trainerDao.update(trainer)).thenReturn(trainer);

        // When
        Trainer updatedTrainer = trainerService.update(trainerId, trainer);

        // Then
        assertNotNull(updatedTrainer);
        assertEquals(trainer, updatedTrainer);
        verify(trainerDao, times(1)).findById(trainerId);
        verify(trainerDao, times(1)).update(trainer);
    }

    @Test
    void testUpdateTrainerNotFound() {
        // Given
        long trainerId = 1L;
        Trainer trainer = new Trainer();
        when(trainerDao.findById(trainerId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(TrainerNotFoundException.class, () -> trainerService.update(trainerId, trainer));
        verify(trainerDao, times(1)).findById(trainerId);
        verify(trainerDao, never()).update(trainer);
    }
}
