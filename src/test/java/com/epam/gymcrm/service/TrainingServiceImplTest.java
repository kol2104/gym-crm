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
    void testSaveTraining() {
        // Given
        Training training = new Training();
        when(traineeDao.findById(training.getTraineeId())).thenReturn(Optional.of(new Trainee()));
        when(trainerDao.findById(training.getTrainerId())).thenReturn(Optional.of(new Trainer()));
        when(trainingDao.save(training)).thenReturn(training);

        // When
        Training savedTraining = trainingService.save(training);

        // Then
        assertNotNull(savedTraining);
        assertEquals(training, savedTraining);
        verify(traineeDao, times(1)).findById(training.getTraineeId());
        verify(trainerDao, times(1)).findById(training.getTrainerId());
        verify(trainingDao, times(1)).save(training);
    }

    @Test
    void testSaveTrainingTraineeNotFound() {
        // Given
        Training training = new Training();
        when(traineeDao.findById(training.getTraineeId())).thenReturn(Optional.empty());

        // When/Then
        assertThrows(TraineeNotFoundException.class, () -> trainingService.save(training));
        verify(traineeDao, times(1)).findById(training.getTraineeId());
        verify(trainerDao, never()).findById(any());
        verify(trainingDao, never()).save(any());
    }

    @Test
    void testSaveTrainingTrainerNotFound() {
        // Given
        Training training = new Training();
        when(traineeDao.findById(training.getTraineeId())).thenReturn(Optional.of(new Trainee()));
        when(trainerDao.findById(training.getTrainerId())).thenReturn(Optional.empty());

        // When/Then
        assertThrows(TrainerNotFoundException.class, () -> trainingService.save(training));
        verify(traineeDao, times(1)).findById(training.getTraineeId());
        verify(trainerDao, times(1)).findById(training.getTrainerId());
        verify(trainingDao, never()).save(any());
    }

    @Test
    void testFindAllTrainings() {
        // Given
        List<Training> trainingList = new ArrayList<>();
        when(trainingDao.findAll()).thenReturn(trainingList);

        // When
        List<Training> foundTrainings = trainingService.findAll();

        // Then
        assertNotNull(foundTrainings);
        assertEquals(trainingList, foundTrainings);
        verify(trainingDao, times(1)).findAll();
    }

    @Test
    void testFindTrainingById() {
        // Given
        long trainingId = 1L;
        Training training = new Training();
        when(trainingDao.findById(trainingId)).thenReturn(Optional.of(training));

        // When
        Training foundTraining = trainingService.findById(trainingId);

        // Then
        assertNotNull(foundTraining);
        assertEquals(training, foundTraining);
        verify(trainingDao, times(1)).findById(trainingId);
    }

    @Test
    void testFindTrainingByIdNotFound() {
        // Given
        long trainingId = 1L;
        when(trainingDao.findById(trainingId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(TrainingNotFoundException.class, () -> trainingService.findById(trainingId));
        verify(trainingDao, times(1)).findById(trainingId);
    }
}
