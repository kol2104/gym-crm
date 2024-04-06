package com.epam.gymcrm.repository;

import com.epam.gymcrm.dao.impl.TrainingDaoDatabase;
import com.epam.gymcrm.model.Training;
import com.epam.gymcrm.model.TrainingCriteria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingDaoDatabaseTest {

    @Mock
    private EntityManager entityManager;
    @Mock
    private CriteriaBuilder criteriaBuilder;
    @Mock
    private CriteriaQuery<Training> criteriaQuery;
    @Mock
    private Root<Training> root;
    @Mock
    private Predicate predicate;
    @Mock
    private TypedQuery<Training> typedQueryTraining;
    @InjectMocks
    private TrainingDaoDatabase trainingDao;

    @Test
    void createTraining() {
        // Test data
        Training training = new Training();

        // Perform the method call
        Training result = trainingDao.create(training);

        // Verify that EntityManager.persist() is called with the correct training
        verify(entityManager, times(1)).persist(training);

        // Assert that the result is the same as the input training
        assertEquals(training, result);
    }

    @Test
    void getAll_ReturnsAllTrainings() {
        // Mock EntityManager.createQuery() and Query.getResultList() to return a list of trainings
        when(entityManager.createQuery(anyString(), eq(Training.class))).thenReturn(typedQueryTraining);
        List<Training> mockTrainings = Collections.singletonList(new Training());
        when(typedQueryTraining.getResultList()).thenReturn(mockTrainings);

        // Perform the method call
        List<Training> result = trainingDao.getAll();

        // Verify that EntityManager.createQuery() is called with the correct query and class
        verify(entityManager, times(1)).createQuery("from Training", Training.class);

        // Assert that the result is not empty
        assertFalse(result.isEmpty());
        // Assert that the result contains the same trainings as the mock data
        assertEquals(mockTrainings, result);
    }

    @Test
    void getById_ReturnsTrainingById_WhenTrainingExists() {
        // Test data
        Long trainingId = 1L;
        Training mockTraining = new Training();
        mockTraining.setId(trainingId);

        // Mock EntityManager.find() to return the mock training
        when(entityManager.find(Training.class, trainingId)).thenReturn(mockTraining);

        // Perform the method call
        Optional<Training> result = trainingDao.getById(trainingId);

        // Verify that EntityManager.find() is called with the correct training ID
        verify(entityManager, times(1)).find(Training.class, trainingId);

        // Assert that the result is not empty
        assertTrue(result.isPresent());
        // Assert that the result is the same as the mock training
        assertEquals(mockTraining, result.get());
    }

    @Test
    void getById_ReturnsEmptyOptional_WhenTrainingDoesNotExist() {
        // Test data
        Long trainingId = 1L;

        // Mock EntityManager.find() to return null
        when(entityManager.find(Training.class, trainingId)).thenReturn(null);

        // Perform the method call
        Optional<Training> result = trainingDao.getById(trainingId);

        // Verify that EntityManager.find() is called with the correct training ID
        verify(entityManager, times(1)).find(Training.class, trainingId);

        // Assert that the result is empty
        assertTrue(result.isEmpty());
    }

    @Test
    void getTrainingsByCriteria_ReturnsTrainingsMatchingCriteria() {
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Training.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Training.class)).thenReturn(root);
        when(criteriaBuilder.conjunction()).thenReturn(predicate);
        when(criteriaQuery.select(any())).thenReturn(criteriaQuery);
        when(entityManager.createQuery(any(CriteriaQuery.class))).thenReturn(typedQueryTraining);
        when(typedQueryTraining.getResultList()).thenReturn(Collections.singletonList(new Training()));

        // Prepare test data
        Map<TrainingCriteria, Object> criteria = new HashMap<>();
        criteria.put(TrainingCriteria.FROM_DATE, LocalDateTime.of(2024, Month.DECEMBER, 23, 14, 30, 0));

        // Perform the method call
        List<Training> result = trainingDao.getTrainingsByCriteria(criteria);

        verify(entityManager).getCriteriaBuilder();
        verify(criteriaBuilder).createQuery(Training.class);
        verify(criteriaQuery).from(Training.class);
        verify(criteriaBuilder).conjunction();
        verify(entityManager).createQuery(criteriaQuery);
        verify(typedQueryTraining).getResultList();

        assertFalse(result.isEmpty());
    }
}
