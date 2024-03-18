package com.epam.gymcrm.service;

import com.epam.gymcrm.dao.TraineeDao;
import com.epam.gymcrm.dao.TrainerDao;
import com.epam.gymcrm.dao.TrainingDao;
import com.epam.gymcrm.exception.TraineeNotFoundException;
import com.epam.gymcrm.exception.TrainerNotFoundException;
import com.epam.gymcrm.exception.TrainingNotFoundException;
import com.epam.gymcrm.model.Trainee;
import com.epam.gymcrm.model.Trainer;
import com.epam.gymcrm.model.Training;
import com.epam.gymcrm.service.impl.TrainingServiceImpl;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private TrainingDao trainingDao;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    @Test
    void testCreateTraining() {
        // Given
        Training training = new Training();
        when(traineeDao.getById(training.getTraineeId())).thenReturn(Optional.of(new Trainee()));
        when(trainerDao.getById(training.getTrainerId())).thenReturn(Optional.of(new Trainer()));
        when(trainingDao.create(training)).thenReturn(training);

        // When
        Training createdTraining = trainingService.create(training);

        // Then
        assertNotNull(createdTraining);
        assertEquals(training, createdTraining);
        verify(traineeDao, times(1)).getById(training.getTraineeId());
        verify(trainerDao, times(1)).getById(training.getTrainerId());
        verify(trainingDao, times(1)).create(training);
    }

    @Test
    void testCreateTraining_TraineeNotFound() {
        // Given
        Training training = new Training();
        when(traineeDao.getById(training.getTraineeId())).thenReturn(Optional.empty());

        // When/Then
        assertThrows(TraineeNotFoundException.class, () -> trainingService.create(training));
        verify(traineeDao, times(1)).getById(training.getTraineeId());
        verify(trainerDao, never()).getById(any());
        verify(trainingDao, never()).create(any());
    }

    @Test
    void testCreateTraining_TrainerNotFound() {
        // Given
        Training training = new Training();
        when(traineeDao.getById(training.getTraineeId())).thenReturn(Optional.of(new Trainee()));
        when(trainerDao.getById(training.getTrainerId())).thenReturn(Optional.empty());

        // When/Then
        assertThrows(TrainerNotFoundException.class, () -> trainingService.create(training));
        verify(traineeDao, times(1)).getById(training.getTraineeId());
        verify(trainerDao, times(1)).getById(training.getTrainerId());
        verify(trainingDao, never()).create(any());
    }

    @Test
    void testGetAllTrainings() {
        // Given
        List<Training> trainingList = new ArrayList<>();
        when(trainingDao.getAll()).thenReturn(trainingList);

        // When
        List<Training> foundTrainings = trainingService.getAll();

        // Then
        assertNotNull(foundTrainings);
        assertEquals(trainingList, foundTrainings);
        verify(trainingDao, times(1)).getAll();
    }

    @Test
    void testGetTrainingById() {
        // Given
        long trainingId = 1L;
        Training training = new Training();
        when(trainingDao.getById(trainingId)).thenReturn(Optional.of(training));

        // When
        Training foundTraining = trainingService.getById(trainingId);

        // Then
        assertNotNull(foundTraining);
        assertEquals(training, foundTraining);
        verify(trainingDao, times(1)).getById(trainingId);
    }

    @Test
    void testGetTrainingByIdNotFound() {
        // Given
        long trainingId = 1L;
        when(trainingDao.getById(trainingId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(TrainingNotFoundException.class, () -> trainingService.getById(trainingId));
        verify(trainingDao, times(1)).getById(trainingId);
    }
}
