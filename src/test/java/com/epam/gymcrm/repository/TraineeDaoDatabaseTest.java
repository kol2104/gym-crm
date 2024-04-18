
package com.epam.gymcrm.repository;

import com.epam.gymcrm.dao.impl.TraineeDaoDatabase;
import com.epam.gymcrm.model.Trainee;
import com.epam.gymcrm.model.Trainer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeDaoDatabaseTest {

    @Mock
    private EntityManager entityManager;
    @Mock
    private TypedQuery<Trainee> typedQueryTrainee;
    @Mock
    private TypedQuery<Trainer> typedQueryTrainer;
    @Mock
    private Query query;
    @InjectMocks
    private TraineeDaoDatabase traineeDao;

    @Test
    void createTrainee() {
        // Test data
        Trainee trainee = new Trainee();

        // Mock EntityManager.persist()
        doNothing().when(entityManager).persist(trainee);

        // Perform the method call
        Trainee createdTrainee = traineeDao.create(trainee);

        // Verify EntityManager.persist() is called
        verify(entityManager, times(1)).persist(trainee);

        // Verify that the created trainee is returned
        assertEquals(trainee, createdTrainee);
    }

    @Test
    void getUnassignedOnTraineeTrainerListByUsername() {
        // Test data
        String username = "testUser";
        List<Trainer> trainers = List.of(new Trainer(), new Trainer());

        // Mock EntityManager.createQuery() and TypedQuery.getResultList()
        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(typedQueryTrainer);
        when(typedQueryTrainer.setParameter("username", username)).thenReturn(typedQueryTrainer);
        when(typedQueryTrainer.getResultList()).thenReturn(trainers);

        // Perform the method call
        List<Trainer> result = traineeDao.getUnassignedOnTraineeTrainerListByUsername(username);

        // Verify EntityManager.createQuery() and TypedQuery.getResultList() are called
        verify(entityManager, times(1)).createQuery(anyString(), eq(Trainer.class));
        verify(typedQueryTrainer, times(1)).setParameter("username", username);
        verify(typedQueryTrainer, times(1)).getResultList();

        // Verify that the list of trainers is returned
        assertEquals(trainers, result);
    }

    @Test
    void getByUsername_ReturnsTrainee_WhenTraineeExists() {
        // Test data
        String username = "testUser";
        Trainee trainee = new Trainee();
        trainee.setUsername(username);

        // Mock EntityManager.createQuery() and TypedQuery.getResultStream().findFirst()
        when(entityManager.createQuery(anyString(), eq(Trainee.class))).thenReturn(typedQueryTrainee);
        when(typedQueryTrainee.setParameter("username", username)).thenReturn(typedQueryTrainee);
        when(typedQueryTrainee.getResultStream()).thenReturn(java.util.stream.Stream.of(trainee));

        // Perform the method call
        Optional<Trainee> result = traineeDao.getByUsername(username);

        // Verify EntityManager.createQuery() and TypedQuery.getResultStream().findFirst() are called
        verify(entityManager, times(1)).createQuery(anyString(), eq(Trainee.class));
        verify(typedQueryTrainee, times(1)).setParameter("username", username);
        verify(typedQueryTrainee, times(1)).getResultStream();

        // Verify that the trainee is returned
        assertTrue(result.isPresent());
        assertEquals(trainee, result.get());
    }

    @Test
    void getByUsername_ReturnsEmptyOptional_WhenTraineeDoesNotExist() {
        // Test data
        String username = "testUser";

        // Mock EntityManager.createQuery() and TypedQuery.getResultStream().findFirst()
        when(entityManager.createQuery(anyString(), eq(Trainee.class))).thenReturn(typedQueryTrainee);
        when(typedQueryTrainee.setParameter("username", username)).thenReturn(typedQueryTrainee);
        when(typedQueryTrainee.getResultStream()).thenReturn(java.util.stream.Stream.empty());

        // Perform the method call
        Optional<Trainee> result = traineeDao.getByUsername(username);

        // Verify EntityManager.createQuery() and TypedQuery.getResultStream().findFirst() are called
        verify(entityManager, times(1)).createQuery(anyString(), eq(Trainee.class));
        verify(typedQueryTrainee, times(1)).setParameter("username", username);
        verify(typedQueryTrainee, times(1)).getResultStream();

        // Verify that the optional is empty
        assertTrue(result.isEmpty());
    }

    @Test
    void getByUsernameAndPassword_ReturnsTrainee_WhenTraineeExists() {
        // Test data
        String username = "testUser";
        String password = "testPassword";
        Trainee trainee = new Trainee();
        trainee.setUsername(username);
        trainee.setPassword(password);

        // Mock EntityManager.createQuery() and TypedQuery.getResultStream().findFirst()
        when(entityManager.createQuery(anyString(), eq(Trainee.class))).thenReturn(typedQueryTrainee);
        when(typedQueryTrainee.setParameter("username", username)).thenReturn(typedQueryTrainee);
        when(typedQueryTrainee.setParameter("password", password)).thenReturn(typedQueryTrainee);
        when(typedQueryTrainee.getResultStream()).thenReturn(java.util.stream.Stream.of(trainee));

        // Perform the method call
        Optional<Trainee> result = traineeDao.getByUsernameAndPassword(username, password);

        // Verify EntityManager.createQuery() and TypedQuery.getResultStream().findFirst() are called
        verify(entityManager, times(1)).createQuery(anyString(), eq(Trainee.class));
        verify(typedQueryTrainee, times(1)).setParameter("username", username);
        verify(typedQueryTrainee, times(1)).setParameter("password", password);
        verify(typedQueryTrainee, times(1)).getResultStream();

        // Verify that the trainee is returned
        assertTrue(result.isPresent());
        assertEquals(trainee, result.get());
    }

    @Test
    void getByUsernameAndPassword_ReturnsEmptyOptional_WhenTraineeDoesNotExist() {
        // Test data
        String username = "nonExistentUser";
        String password = "nonExistentPassword";

        // Mock EntityManager.createQuery() and TypedQuery.getResultStream().findFirst()
        when(entityManager.createQuery(anyString(), eq(Trainee.class))).thenReturn(typedQueryTrainee);
        when(typedQueryTrainee.setParameter("username", username)).thenReturn(typedQueryTrainee);
        when(typedQueryTrainee.setParameter("password", password)).thenReturn(typedQueryTrainee);
        when(typedQueryTrainee.getResultStream()).thenReturn(java.util.stream.Stream.empty());

        // Perform the method call
        Optional<Trainee> result = traineeDao.getByUsernameAndPassword(username, password);

        // Verify EntityManager.createQuery() and TypedQuery.getResultStream().findFirst() are called
        verify(entityManager, times(1)).createQuery(anyString(), eq(Trainee.class));
        verify(typedQueryTrainee, times(1)).setParameter("username", username);
        verify(typedQueryTrainee, times(1)).setParameter("password", password);
        verify(typedQueryTrainee, times(1)).getResultStream();

        // Verify that the result is an empty Optional
        assertTrue(result.isEmpty());
    }

    @Test
    void update_ReturnsUpdatedTrainee_WhenTraineeExists() {
        // Test data
        Trainee trainee = new Trainee();
        trainee.setId(1L);
        trainee.setUsername("testUser");

        // Mock EntityManager.find() to return the trainee
        when(entityManager.find(Trainee.class, 1L)).thenReturn(trainee);

        // Perform the method call
        Trainee updatedTrainee = traineeDao.update(trainee);

        // Verify that EntityManager.find() is called
        verify(entityManager, times(1)).find(Trainee.class, 1L);

        // Verify that the trainee is updated successfully
        assertNotNull(updatedTrainee);
        assertEquals(trainee, updatedTrainee);
    }

    @Test
    void update_ReturnsNull_WhenTraineeDoesNotExist() {
        // Test data
        Trainee trainee = new Trainee();
        trainee.setId(1L);
        trainee.setUsername("testUser");

        // Mock EntityManager.find() to return null
        when(entityManager.find(Trainee.class, 1L)).thenReturn(null);

        // Perform the method call
        Trainee updatedTrainee = traineeDao.update(trainee);

        // Verify that EntityManager.find() is called
        verify(entityManager, times(1)).find(Trainee.class, 1L);

        // Verify that null is returned when trainee doesn't exist
        assertNull(updatedTrainee);
    }

    @Test
    void delete_DeletesTrainee_WhenTraineeExists() {
        // Test data
        long id = 1L;

        // Mock EntityManager.createQuery() to return the query
        when(entityManager.createQuery(anyString())).thenReturn(query);
        // Mock Query.setParameter() and Query.executeUpdate() to return 1
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        // Perform the method call
        traineeDao.delete(id);

        // Verify that EntityManager.createQuery() is called with the correct query
        verify(entityManager, times(1)).createQuery("delete from Trainee t where t.id = :id");
        // Verify that Query.setParameter() is called with the correct parameters
        verify(query, times(1)).setParameter("id", id);
        // Verify that Query.executeUpdate() is called
        verify(query, times(1)).executeUpdate();
    }

    @Test
    void deleteByUsername_DeletesTrainee_WhenTraineeExists() {
        // Test data
        String username = "testUser";

        // Mock EntityManager.createQuery() to return the query
        when(entityManager.createQuery(anyString())).thenReturn(query);
        // Mock Query.setParameter() and Query.executeUpdate() to return 1
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        // Perform the method call
        traineeDao.deleteByUsername(username);

        // Verify that EntityManager.createQuery() is called with the correct query
        verify(entityManager, times(1)).createQuery("delete from Trainee t where t.username = :username");
        // Verify that Query.setParameter() is called with the correct parameters
        verify(query, times(1)).setParameter("username", username);
        // Verify that Query.executeUpdate() is called
        verify(query, times(1)).executeUpdate();
    }
}

