package com.epam.gymcrm.repository;

import com.epam.gymcrm.dao.impl.TrainingTypeDaoDatabase;
import com.epam.gymcrm.model.TrainingType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingTypeDaoDatabaseTest {

    @Mock
    private EntityManager entityManager;
    @Mock
    private TypedQuery<TrainingType> typedQueryTrainingType;
    @InjectMocks
    private TrainingTypeDaoDatabase trainingTypeDao;

    @Test
    void getAllTrainees() {
        // Test data
        List<TrainingType> trainees = List.of(new TrainingType(), new TrainingType());

        // Mock EntityManager.createQuery() and Query.getResultList()
        when(entityManager.createQuery("from TrainingType", TrainingType.class)).thenReturn(typedQueryTrainingType);
        when(typedQueryTrainingType.getResultList()).thenReturn(trainees);

        // Perform the method call
        List<TrainingType> allTrainingTypes = trainingTypeDao.getAll();

        // Verify EntityManager.createQuery() and Query.getResultList() are called
        verify(entityManager, times(1)).createQuery("from TrainingType", TrainingType.class);
        verify(typedQueryTrainingType, times(1)).getResultList();

        // Verify that the list of trainees is returned
        assertEquals(trainees, allTrainingTypes);
    }

    @Test
    void getById_ReturnsTrainee_WhenTraineeExists() {
        // Test data
        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);

        // Mock EntityManager.find()
        when(entityManager.find(TrainingType.class, 1L)).thenReturn(trainingType);

        // Perform the method call
        Optional<TrainingType> foundTrainingType = trainingTypeDao.getById(1L);

        // Verify EntityManager.find() is called
        verify(entityManager, times(1)).find(TrainingType.class, 1L);

        // Verify that the trainingType is found and returned
        assertTrue(foundTrainingType.isPresent());
        assertEquals(trainingType, foundTrainingType.get());
    }

    @Test
    void getById_ReturnsEmptyOptional_WhenTraineeDoesNotExist() {
        // Mock EntityManager.find() for a non-existent trainingType
        when(entityManager.find(TrainingType.class, 1L)).thenReturn(null);

        // Perform the method call
        Optional<TrainingType> foundTrainingType = trainingTypeDao.getById(1L);

        // Verify EntityManager.find() is called
        verify(entityManager, times(1)).find(TrainingType.class, 1L);

        // Verify that the trainingType is not found
        assertFalse(foundTrainingType.isPresent());
    }

    @Test
    void getByName_ReturnsTrainee_WhenTraineeExists() {
        // Test data
        String name = "running";
        TrainingType trainee = new TrainingType();
        trainee.setName(name);

        // Mock EntityManager.createQuery() and TypedQuery.getResultStream().findFirst()
        when(entityManager.createQuery(anyString(), eq(TrainingType.class))).thenReturn(typedQueryTrainingType);
        when(typedQueryTrainingType.setParameter("name", name)).thenReturn(typedQueryTrainingType);
        when(typedQueryTrainingType.getResultStream()).thenReturn(java.util.stream.Stream.of(trainee));

        // Perform the method call
        Optional<TrainingType> result = trainingTypeDao.getByName(name);

        // Verify EntityManager.createQuery() and TypedQuery.getResultStream().findFirst() are called
        verify(entityManager, times(1)).createQuery(anyString(), eq(TrainingType.class));
        verify(typedQueryTrainingType, times(1)).setParameter("name", name);
        verify(typedQueryTrainingType, times(1)).getResultStream();

        // Verify that the trainingType is returned
        assertTrue(result.isPresent());
        assertEquals(trainee, result.get());
    }

    @Test
    void getByName_ReturnsEmptyOptional_WhenTraineeDoesNotExist() {
        // Test data
        String name = "running";

        // Mock EntityManager.createQuery() and TypedQuery.getResultStream().findFirst()
        when(entityManager.createQuery(anyString(), eq(TrainingType.class))).thenReturn(typedQueryTrainingType);
        when(typedQueryTrainingType.setParameter("name", name)).thenReturn(typedQueryTrainingType);
        when(typedQueryTrainingType.getResultStream()).thenReturn(java.util.stream.Stream.empty());

        // Perform the method call
        Optional<TrainingType> result = trainingTypeDao.getByName(name);

        // Verify EntityManager.createQuery() and TypedQuery.getResultStream().findFirst() are called
        verify(entityManager, times(1)).createQuery(anyString(), eq(TrainingType.class));
        verify(typedQueryTrainingType, times(1)).setParameter("name", name);
        verify(typedQueryTrainingType, times(1)).getResultStream();

        // Verify that the optional is empty
        assertTrue(result.isEmpty());
    }
}
