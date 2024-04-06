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
import com.epam.gymcrm.model.TrainingCriteria;
import com.epam.gymcrm.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    void testCreateTrainingSuccessfully() {
        Trainee trainee = new Trainee();
        trainee.setId(1L);
        Trainer trainer = new Trainer();
        trainer.setId(2L);
        Training training = new Training();
        training.setTrainee(trainee);
        training.setTrainer(trainer);

        when(traineeDao.getById(trainee.getId())).thenReturn(Optional.of(trainee));
        when(trainerDao.getById(trainer.getId())).thenReturn(Optional.of(trainer));
        when(trainingDao.create(training)).thenReturn(training);

        assertDoesNotThrow(() -> trainingService.create(training));
        verify(traineeDao, times(1)).getById(trainee.getId());
        verify(trainerDao, times(1)).getById(trainer.getId());
        verify(trainingDao, times(1)).create(training);
    }

    @Test
    void testCreateTrainingWithNonExistingTrainee() {
        Trainee trainee = new Trainee();
        trainee.setId(1L);
        Trainer trainer = new Trainer();
        trainer.setId(2L);
        Training training = new Training();
        training.setTrainee(trainee);
        training.setTrainer(trainer);

        when(traineeDao.getById(trainee.getId())).thenReturn(Optional.empty());

        assertThrows(TraineeNotFoundException.class, () -> trainingService.create(training));
        verify(traineeDao, times(1)).getById(trainee.getId());
        verify(trainerDao, never()).getById(any());
        verify(trainingDao, never()).create(any());
    }

    @Test
    void testCreateTrainingWithNonExistingTrainer() {
        Trainee trainee = new Trainee();
        trainee.setId(1L);
        Trainer trainer = new Trainer();
        trainer.setId(2L);
        Training training = new Training();
        training.setTrainee(trainee);
        training.setTrainer(trainer);

        when(traineeDao.getById(trainee.getId())).thenReturn(Optional.of(trainee));
        when(trainerDao.getById(trainer.getId())).thenReturn(Optional.empty());

        assertThrows(TrainerNotFoundException.class, () -> trainingService.create(training));
        verify(traineeDao, times(1)).getById(trainee.getId());
        verify(trainerDao, times(1)).getById(trainer.getId());
        verify(trainingDao, never()).create(any());
    }

    @Test
    void testGetAllTrainings() {
        // Mocking the data returned by the DAO
        Training training1 = new Training();
        Training training2 = new Training();
        List<Training> expectedTrainings = Arrays.asList(training1, training2);
        when(trainingDao.getAll()).thenReturn(expectedTrainings);

        // Calling the service method
        List<Training> actualTrainings = trainingService.getAll();

        // Verifying that the DAO method was called
        verify(trainingDao, times(1)).getAll();

        // Asserting that the returned list matches the expected list
        assertEquals(expectedTrainings, actualTrainings);
    }

    @Test
    void testGetTrainingById() {
        // Mocking the training object and its ID
        Long trainingId = 1L;
        Training expectedTraining = new Training();
        expectedTraining.setId(trainingId);

        // Mocking the DAO to return the training when getById is called
        when(trainingDao.getById(trainingId)).thenReturn(Optional.of(expectedTraining));

        // Calling the service method
        Training actualTraining = trainingService.getById(trainingId);

        // Verifying that the DAO method was called
        verify(trainingDao, times(1)).getById(trainingId);

        // Asserting that the returned training matches the expected training
        assertEquals(expectedTraining, actualTraining);
    }

    @Test
    void testGetTrainingByNonExistingId() {
        // Mocking a non-existing training ID
        Long nonExistingTrainingId = 999L;

        // Mocking the DAO to return empty optional when getById is called with the non-existing ID
        when(trainingDao.getById(nonExistingTrainingId)).thenReturn(Optional.empty());

        // Calling the service method and asserting that it throws TrainingNotFoundException
        assertThrows(TrainingNotFoundException.class, () -> trainingService.getById(nonExistingTrainingId));

        // Verifying that the DAO method was called
        verify(trainingDao, times(1)).getById(nonExistingTrainingId);
    }

    @Test
    void testGetTrainingByTraineeUsernameAndCriteria() {
        String username = "testUser";
        Map<TrainingCriteria, Object> criteria = new HashMap<>();
        criteria.put(TrainingCriteria.FROM_DATE, new Date());
        criteria.put(TrainingCriteria.TO_DATE, new Date());

        when(trainingDao.getTrainingsByCriteria(any())).thenReturn(Collections.emptyList());

        List<Training> trainings = trainingService.getTrainingByTraineeUsernameAndCriteria(username, criteria);
        assertNotNull(trainings);
        assertTrue(trainings.isEmpty());
        verify(trainingDao, times(1)).getTrainingsByCriteria(any());
    }

    @Test
    void testGetTrainingByTrainerUsernameAndCriteria() {
        String username = "testUser";
        Map<TrainingCriteria, Object> criteria = new HashMap<>();
        criteria.put(TrainingCriteria.FROM_DATE, new Date());
        criteria.put(TrainingCriteria.TO_DATE, new Date());

        when(trainingDao.getTrainingsByCriteria(any())).thenReturn(Collections.emptyList());

        List<Training> trainings = trainingService.getTrainingByTrainerUsernameAndCriteria(username, criteria);
        assertNotNull(trainings);
        assertTrue(trainings.isEmpty());
        verify(trainingDao, times(1)).getTrainingsByCriteria(any());
    }

    @Test
    void testGetTrainingByTraineeUsernameAndCriteriaWithNullCriteria() {
        String username = "testUser";

        when(trainingDao.getTrainingsByCriteria(any())).thenReturn(Collections.emptyList());

        List<Training> trainings = trainingService.getTrainingByTraineeUsernameAndCriteria(username, null);
        assertNotNull(trainings);
        assertTrue(trainings.isEmpty());
        verify(trainingDao, times(1)).getTrainingsByCriteria(any());
    }

    @Test
    void testGetTrainingByTrainerUsernameAndCriteriaWithNullCriteria() {
        String username = "testUser";

        when(trainingDao.getTrainingsByCriteria(any())).thenReturn(Collections.emptyList());

        List<Training> trainings = trainingService.getTrainingByTrainerUsernameAndCriteria(username, null);
        assertNotNull(trainings);
        assertTrue(trainings.isEmpty());
        verify(trainingDao, times(1)).getTrainingsByCriteria(any());
    }
}
