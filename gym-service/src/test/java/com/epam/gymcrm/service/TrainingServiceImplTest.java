package com.epam.gymcrm.service;

import com.epam.gymcrm.dao.TraineeDao;
import com.epam.gymcrm.dao.TrainerDao;
import com.epam.gymcrm.dao.TrainingDao;
import com.epam.gymcrm.dao.TrainingTypeDao;
import com.epam.gymcrm.dto.training.TrainingDto;
import com.epam.gymcrm.dto.training.TrainingToDeleteRequestDto;
import com.epam.gymcrm.exception.TraineeNotFoundException;
import com.epam.gymcrm.exception.TrainerNotFoundException;
import com.epam.gymcrm.mapper.TrainingMapper;
import com.epam.gymcrm.model.Trainee;
import com.epam.gymcrm.model.Trainer;
import com.epam.gymcrm.model.Training;
import com.epam.gymcrm.model.TrainingCriteria;
import com.epam.gymcrm.model.TrainingType;
import com.epam.gymcrm.rest.TrainingsReportService;
import com.epam.gymcrm.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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
    @Mock
    private TrainingTypeDao trainingTypeDao;
    @Mock
    private TrainingMapper trainingMapper;
    @Mock
    private TrainingsReportService trainingsReportService;
    @InjectMocks
    private TrainingServiceImpl trainingService;

    @Test
    void testCreateTrainingSuccessfully() {
        TrainingDto trainingDto = new TrainingDto(null, null, null, null, null, null);
        Trainee trainee = new Trainee();
        trainee.setUsername("trainee");
        Trainer trainer = new Trainer();
        trainer.setUsername("trainer");
        TrainingType trainingType = new TrainingType();
        trainingType.setName("name");
        Training training = new Training();
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingType(trainingType);
        training.setTrainingDate(LocalDateTime.MIN);

        when(traineeDao.getByUsername(trainee.getUsername())).thenReturn(Optional.of(trainee));
        when(trainerDao.getByUsername(trainer.getUsername())).thenReturn(Optional.of(trainer));
        when(trainingTypeDao.getByName(trainingType.getName())).thenReturn(Optional.of(trainingType));
        when(trainingDao.create(training)).thenReturn(training);
        when(trainingMapper.dtoToModel(trainingDto)).thenReturn(training);
        when(trainingDao.getTrainingsByCriteria(any())).thenReturn(new ArrayList<>());

        assertDoesNotThrow(() -> trainingService.create(trainingDto));
        verify(traineeDao, times(1)).getByUsername(trainee.getUsername());
        verify(trainerDao, times(1)).getByUsername(trainer.getUsername());
        verify(trainingDao, times(1)).create(training);
        verify(trainingMapper).dtoToModel(trainingDto);
        verify(trainingsReportService).updateTrainerWorkload(any());
    }

    @Test
    void testCreateTrainingWithNonExistingTrainee() {
        TrainingDto trainingDto = new TrainingDto(null, null, null, null, null, null);
        Trainee trainee = new Trainee();
        trainee.setUsername("trainee");
        Trainer trainer = new Trainer();
        trainer.setUsername("trainer");
        Training training = new Training();
        training.setTrainee(trainee);
        training.setTrainer(trainer);

        when(traineeDao.getByUsername(trainee.getUsername())).thenReturn(Optional.empty());
        when(trainingMapper.dtoToModel(trainingDto)).thenReturn(training);

        assertThrows(TraineeNotFoundException.class, () -> trainingService.create(trainingDto));
        verify(traineeDao, times(1)).getByUsername(trainee.getUsername());
        verify(trainerDao, never()).getByUsername(any());
        verify(trainingDao, never()).create(any());
        verify(trainingMapper).dtoToModel(trainingDto);
    }

    @Test
    void testCreateTrainingWithNonExistingTrainer() {
        TrainingDto trainingDto = new TrainingDto(null, null, null, null, null, null);
        Trainee trainee = new Trainee();
        trainee.setUsername("trainee");
        Trainer trainer = new Trainer();
        trainer.setUsername("trainer");
        Training training = new Training();
        training.setTrainee(trainee);
        training.setTrainer(trainer);

        when(traineeDao.getByUsername(trainee.getUsername())).thenReturn(Optional.of(trainee));
        when(trainerDao.getByUsername(trainer.getUsername())).thenReturn(Optional.empty());
        when(trainingMapper.dtoToModel(trainingDto)).thenReturn(training);

        assertThrows(TrainerNotFoundException.class, () -> trainingService.create(trainingDto));
        verify(traineeDao, times(1)).getByUsername(trainee.getUsername());
        verify(trainerDao, times(1)).getByUsername(trainer.getUsername());
        verify(trainingDao, never()).create(any());
        verify(trainingMapper).dtoToModel(trainingDto);
    }

    @Test
    void testGetTrainingByTraineeUsernameAndCriteria() {
        String username = "testUser";
        Map<TrainingCriteria, String> criteria = new HashMap<>();
        criteria.put(TrainingCriteria.FROM_DATE, "2024-01-01 10:00:00");
        criteria.put(TrainingCriteria.TO_DATE, "2025-01-01 10:00:00");

        when(trainingDao.getTrainingsByCriteria(any())).thenReturn(Collections.emptyList());

        List<TrainingDto> trainings = trainingService.getTrainingByTraineeUsernameAndCriteria(username, criteria);
        assertNotNull(trainings);
        assertTrue(trainings.isEmpty());
        verify(trainingDao, times(1)).getTrainingsByCriteria(any());
    }

    @Test
    void testGetTrainingByTrainerUsernameAndCriteria() {
        String username = "testUser";
        Map<TrainingCriteria, String> criteria = new HashMap<>();
        criteria.put(TrainingCriteria.FROM_DATE, "2024-01-01 10:00:00");
        criteria.put(TrainingCriteria.TO_DATE, "2025-01-01 10:00:00");

        when(trainingDao.getTrainingsByCriteria(any())).thenReturn(Collections.emptyList());

        List<TrainingDto> trainings = trainingService.getTrainingByTrainerUsernameAndCriteria(username, criteria);
        assertNotNull(trainings);
        assertTrue(trainings.isEmpty());
        verify(trainingDao, times(1)).getTrainingsByCriteria(any());
    }

    @Test
    void testGetTrainingByTraineeUsernameAndCriteriaWithNullCriteria() {
        String username = "testUser";

        when(trainingDao.getTrainingsByCriteria(any())).thenReturn(Collections.emptyList());

        List<TrainingDto> trainings = trainingService.getTrainingByTraineeUsernameAndCriteria(username, null);
        assertNotNull(trainings);
        assertTrue(trainings.isEmpty());
        verify(trainingDao, times(1)).getTrainingsByCriteria(any());
    }

    @Test
    void testGetTrainingByTrainerUsernameAndCriteriaWithNullCriteria() {
        String username = "testUser";

        when(trainingDao.getTrainingsByCriteria(any())).thenReturn(Collections.emptyList());

        List<TrainingDto> trainings = trainingService.getTrainingByTrainerUsernameAndCriteria(username, null);
        assertNotNull(trainings);
        assertTrue(trainings.isEmpty());
        verify(trainingDao, times(1)).getTrainingsByCriteria(any());
    }

    @Test
    void testDelete() {
        LocalDateTime trainingDate = LocalDateTime.now();
        TrainingToDeleteRequestDto dto = new TrainingToDeleteRequestDto(trainingDate, "traineeUser", "trainerUser");

        List<Training> mockTrainings = new ArrayList<>();
        Trainee trainee = new Trainee();
        trainee.setUsername("trainee");
        Trainer trainer = new Trainer();
        trainer.setUsername("trainer");
        Training mockTraining = new Training(); // Assume this has methods to set properties
        mockTraining.setId(1L);
        mockTraining.setTrainee(trainee);
        mockTraining.setTrainer(trainer);
        mockTrainings.add(mockTraining);
        when(trainingDao.getTrainingsByCriteria(any())).thenReturn(mockTrainings);

        trainingService.delete(dto);

        verify(trainingDao, times(1)).getTrainingsByCriteria(any());
        verify(trainingDao, times(1)).delete(mockTraining.getId());
        verify(trainingsReportService, times(1)).updateTrainerWorkload(any());
        verify(trainingsReportService).updateTrainerWorkload(any());
    }
}
