package com.epam.gymcrm.service;

import com.epam.gymcrm.dao.TraineeDao;
import com.epam.gymcrm.exception.TraineeNotFoundException;
import com.epam.gymcrm.model.Trainee;
import com.epam.gymcrm.service.impl.TraineeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {

    @Mock
    private TraineeDao traineeDao;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @Test
    void testSaveTrainee() {
        // Given
        Trainee trainee = new Trainee();
        when(traineeDao.save(trainee)).thenReturn(trainee);

        // When
        Trainee savedTrainee = traineeService.save(trainee);

        // Then
        assertNotNull(savedTrainee);
        assertEquals(trainee, savedTrainee);
        verify(traineeDao, times(1)).save(trainee);
    }

    @Test
    void testFindAllTrainees() {
        // Given
        List<Trainee> expectedTrainees = Arrays.asList(new Trainee(), new Trainee());
        when(traineeDao.findAll()).thenReturn(expectedTrainees);

        // When
        List<Trainee> actualTrainees = traineeService.findAll();

        // Then
        assertNotNull(actualTrainees);
        assertEquals(expectedTrainees.size(), actualTrainees.size());
        assertTrue(actualTrainees.containsAll(expectedTrainees));
        verify(traineeDao, times(1)).findAll();
    }

    @Test
    void testFindByIdTraineeFound() {
        // Given
        long traineeId = 1L;
        Trainee trainee = new Trainee();
        when(traineeDao.findById(traineeId)).thenReturn(Optional.of(trainee));

        // When
        Trainee foundTrainee = traineeService.findById(traineeId);

        // Then
        assertNotNull(foundTrainee);
        assertEquals(trainee, foundTrainee);
        verify(traineeDao, times(1)).findById(traineeId);
    }

    @Test
    void testFindByIdTraineeNotFound() {
        // Given
        long traineeId = 1L;
        when(traineeDao.findById(traineeId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(TraineeNotFoundException.class, () -> traineeService.findById(traineeId));
        verify(traineeDao, times(1)).findById(traineeId);
    }

    @Test
    void testDeleteTrainee() {
        // Given
        long traineeId = 1L;

        // When
        traineeService.delete(traineeId);

        // Then
        verify(traineeDao, times(1)).delete(traineeId);
    }

    @Test
    void testUpdateTrainee() {
        // Given
        long traineeId = 1L;
        Trainee trainee = new Trainee();
        trainee.setId(traineeId);
        when(traineeDao.findById(traineeId)).thenReturn(Optional.of(trainee));
        when(traineeDao.update(trainee)).thenReturn(trainee);

        // When
        Trainee updatedTrainee = traineeService.update(traineeId, trainee);

        // Then
        assertNotNull(updatedTrainee);
        assertEquals(trainee, updatedTrainee);
        verify(traineeDao, times(1)).findById(traineeId);
        verify(traineeDao, times(1)).update(trainee);
    }

    @Test
    void testUpdateTraineeNotFound() {
        // Given
        long traineeId = 1L;
        Trainee trainee = new Trainee();
        when(traineeDao.findById(traineeId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(TraineeNotFoundException.class, () -> traineeService.update(traineeId, trainee));
        verify(traineeDao, times(1)).findById(traineeId);
        verify(traineeDao, never()).update(trainee);
    }
}
