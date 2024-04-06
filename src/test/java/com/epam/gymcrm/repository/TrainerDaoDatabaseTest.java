package com.epam.gymcrm.repository;

import com.epam.gymcrm.dao.impl.TrainerDaoDatabase;
import com.epam.gymcrm.model.Trainer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerDaoDatabaseTest {

    @Mock
    private EntityManager entityManager;
    @Mock
    private TypedQuery<Trainer> typedQueryTrainer;
    @InjectMocks
    private TrainerDaoDatabase trainerDao;

    @Test
    void createTrainer() {
        // Test data
        Trainer trainer = new Trainer();

        // Perform the method call
        Trainer result = trainerDao.create(trainer);

        // Verify that EntityManager.persist() is called with the correct trainer
        verify(entityManager, times(1)).persist(trainer);
        // Assert that the returned trainer matches the expected trainer
        assertSame(trainer, result);
    }

    @Test
    void getAll_ReturnsListOfTrainers_WhenTrainersExist() {
        // Test data
        List<Trainer> trainers = List.of(new Trainer(), new Trainer());

        // Mock EntityManager.createQuery() to return the query
        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(typedQueryTrainer);
        // Mock Query.getResultList() to return the list of trainers
        when(typedQueryTrainer.getResultList()).thenReturn(trainers);

        // Perform the method call
        List<Trainer> result = trainerDao.getAll();

        // Verify that EntityManager.createQuery() is called with the correct query and class
        verify(entityManager, times(1)).createQuery("from Trainer", Trainer.class);
        // Verify that Query.getResultList() is called
        verify(typedQueryTrainer, times(1)).getResultList();

        // Assert that the returned list of trainers is not empty
        assertFalse(result.isEmpty());
        // Assert that the returned list of trainers contains all trainers
        assertSame(trainers, result);
    }

    @Test
    void getById_ReturnsTrainer_WhenTrainerExists() {
        // Test data
        long trainerId = 1L;
        Trainer trainer = new Trainer();

        // Mock EntityManager.find() to return the trainer
        when(entityManager.find(Trainer.class, trainerId)).thenReturn(trainer);

        // Perform the method call
        Optional<Trainer> result = trainerDao.getById(trainerId);

        // Verify that EntityManager.find() is called with the correct trainer id
        verify(entityManager, times(1)).find(Trainer.class, trainerId);

        // Assert that the returned optional contains the trainer
        assertTrue(result.isPresent());
        assertSame(trainer, result.get());
    }

    @Test
    void getById_ReturnsEmptyOptional_WhenTrainerDoesNotExist() {
        // Test data
        long trainerId = 1L;

        // Mock EntityManager.find() to return null
        when(entityManager.find(Trainer.class, trainerId)).thenReturn(null);

        // Perform the method call
        Optional<Trainer> result = trainerDao.getById(trainerId);

        // Verify that EntityManager.find() is called with the correct trainer id
        verify(entityManager, times(1)).find(Trainer.class, trainerId);

        // Assert that the returned optional is empty
        assertTrue(result.isEmpty());
    }

    @Test
    void getByUsername_ReturnsTrainer_WhenTrainerExists() {
        // Test data
        String username = "trainer1";
        Trainer trainer = new Trainer();

        // Mock EntityManager.createQuery() to return the query
        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(typedQueryTrainer);
        // Mock Query.setParameter() and Query.getResultStream() to return a Stream
        when(typedQueryTrainer.setParameter(anyString(), any())).thenReturn(typedQueryTrainer);
        when(typedQueryTrainer.getResultStream()).thenReturn(Stream.of(trainer));

        // Perform the method call
        Optional<Trainer> result = trainerDao.getByUsername(username);

        // Verify that EntityManager.createQuery() is called with the correct query and class
        verify(entityManager, times(1)).createQuery("from Trainer t where t.username = :username", Trainer.class);
        // Verify that Query.setParameter() is called with the correct parameters
        verify(typedQueryTrainer, times(1)).setParameter("username", username);
        // Verify that Query.getResultStream() is called
        verify(typedQueryTrainer, times(1)).getResultStream();

        // Assert that the result is not empty
        assertTrue(result.isPresent());
        // Assert that the returned trainer matches the expected trainer
        assertSame(trainer, result.get());
    }

    @Test
    void getByUsername_ReturnsEmptyOptional_WhenTrainerDoesNotExist() {
        // Test data
        String username = "nonexistent_trainer";

        // Mock EntityManager.createQuery() to return the query
        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(typedQueryTrainer);
        // Mock Query.setParameter() and Query.getResultStream() to return an empty Stream
        when(typedQueryTrainer.setParameter(anyString(), any())).thenReturn(typedQueryTrainer);
        when(typedQueryTrainer.getResultStream()).thenReturn(Stream.empty());

        // Perform the method call
        Optional<Trainer> result = trainerDao.getByUsername(username);

        // Verify that EntityManager.createQuery() is called with the correct query and class
        verify(entityManager, times(1)).createQuery("from Trainer t where t.username = :username", Trainer.class);
        // Verify that Query.setParameter() is called with the correct parameters
        verify(typedQueryTrainer, times(1)).setParameter("username", username);
        // Verify that Query.getResultStream() is called
        verify(typedQueryTrainer, times(1)).getResultStream();

        // Assert that the result is empty
        assertFalse(result.isPresent());
    }

    @Test
    void getByUsernameAndPassword_ReturnsTrainer_WhenTrainerExists() {
        // Test data
        String username = "testUser";
        String password = "testPassword";
        Trainer trainer = new Trainer();

        // Mock Query.setParameter() and Query.getResultStream().findFirst() to return a non-empty Optional
        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(typedQueryTrainer);
        when(typedQueryTrainer.setParameter(anyString(), any())).thenReturn(typedQueryTrainer);
        when(typedQueryTrainer.getResultStream()).thenReturn(Stream.of(trainer)); // Adjusted stubbing

        // Perform the method call
        Optional<Trainer> result = trainerDao.getByUsernameAndPassword(username, password);

        // Verify that EntityManager.createQuery() is called with the correct query and class
        verify(entityManager, times(1)).createQuery("from Trainer t where t.username = :username and t.password = :password", Trainer.class);
        // Verify that Query.setParameter() is called with the correct parameters
        verify(typedQueryTrainer, times(1)).setParameter("username", username);
        verify(typedQueryTrainer, times(1)).setParameter("password", password);
        // Verify that Query.getResultStream() is called
        verify(typedQueryTrainer, times(1)).getResultStream();

        // Assert that the result is not empty
        assertTrue(result.isPresent());
    }

    @Test
    void getByUsernameAndPassword_ReturnsEmptyOptional_WhenTrainerDoesNotExist() {
        // Test data
        String username = "testUser";
        String password = "testPassword";

        // Mock Query.setParameter() and Query.getResultStream().findFirst() to return an empty Optional
        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(typedQueryTrainer);
        when(typedQueryTrainer.setParameter(anyString(), any())).thenReturn(typedQueryTrainer);
        when(typedQueryTrainer.getResultStream()).thenReturn(Stream.empty()); // Adjusted stubbing

        // Perform the method call
        Optional<Trainer> result = trainerDao.getByUsernameAndPassword(username, password);

        // Verify that EntityManager.createQuery() is called with the correct query and class
        verify(entityManager, times(1)).createQuery("from Trainer t where t.username = :username and t.password = :password", Trainer.class);
        // Verify that Query.setParameter() is called with the correct parameters
        verify(typedQueryTrainer, times(1)).setParameter("username", username);
        verify(typedQueryTrainer, times(1)).setParameter("password", password);
        // Verify that Query.getResultStream() is called
        verify(typedQueryTrainer, times(1)).getResultStream();

        // Assert that the result is empty
        assertTrue(result.isEmpty());
    }

    @Test
    void update_ReturnsUpdatedTrainer_WhenTrainerExists() {
        // Test data
        Trainer trainer = new Trainer();
        trainer.setId(1L);

        // Mock EntityManager.find() to return a non-null trainer
        when(entityManager.find(Trainer.class, trainer.getId())).thenReturn(trainer);
        // Mock EntityManager.merge() to return the updated trainer
        when(entityManager.merge(trainer)).thenReturn(trainer);

        // Perform the method call
        Trainer result = trainerDao.update(trainer);

        // Verify that EntityManager.find() is called with the correct entity class and ID
        verify(entityManager, times(1)).find(Trainer.class, trainer.getId());
        // Verify that EntityManager.merge() is called with the correct trainer
        verify(entityManager, times(1)).merge(trainer);

        // Assert that the result is not null
        assertNotNull(result);
        // Assert that the result is the same as the input trainer
        assertEquals(trainer, result);
    }

    @Test
    void update_ReturnsNull_WhenTrainerDoesNotExist() {
        // Test data
        Trainer trainer = new Trainer();
        trainer.setId(1L);

        // Mock EntityManager.find() to return null
        when(entityManager.find(Trainer.class, trainer.getId())).thenReturn(null);

        // Perform the method call
        Trainer result = trainerDao.update(trainer);

        // Verify that EntityManager.find() is called with the correct entity class and ID
        verify(entityManager, times(1)).find(Trainer.class, trainer.getId());
        // Verify that EntityManager.merge() is not called
        verify(entityManager, never()).merge(any());

        // Assert that the result is null
        assertNull(result);
    }

    @Test
    void getByFirstNameAndLastName_ReturnsTrainer_WhenTrainerExists() {
        // Test data
        String firstName = "John";
        String lastName = "Doe";
        Trainer trainer = new Trainer();

        // Mock Query.setParameter() and Query.getResultStream().findFirst() to return a non-empty Optional
        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(typedQueryTrainer);
        when(typedQueryTrainer.setParameter(anyString(), any())).thenReturn(typedQueryTrainer);
        when(typedQueryTrainer.getResultStream()).thenReturn(Stream.of(trainer)); // Adjusted stubbing

        // Perform the method call
        Optional<Trainer> result = trainerDao.getByFirstNameAndLastName(firstName, lastName);

        // Verify that EntityManager.createQuery() is called with the correct query and class
        verify(entityManager, times(1)).createQuery("from Trainer t where t.firstName = :firstName and t.lastName = :lastName", Trainer.class);
        // Verify that Query.setParameter() is called with the correct parameters
        verify(typedQueryTrainer, times(1)).setParameter("firstName", firstName);
        verify(typedQueryTrainer, times(1)).setParameter("lastName", lastName);
        // Verify that Query.getResultStream() is called
        verify(typedQueryTrainer, times(1)).getResultStream();

        // Assert that the result is not empty
        assertTrue(result.isPresent());
        // Assert that the result is the same as the input trainer
        assertEquals(trainer, result.get());
    }

    @Test
    void getByFirstNameAndLastName_ReturnsEmptyOptional_WhenTrainerDoesNotExist() {
        // Test data
        String firstName = "John";
        String lastName = "Doe";

        // Mock Query.setParameter() and Query.getResultStream().findFirst() to return an empty Optional
        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(typedQueryTrainer);
        when(typedQueryTrainer.setParameter(anyString(), any())).thenReturn(typedQueryTrainer);
        when(typedQueryTrainer.getResultStream()).thenReturn(Stream.empty()); // Adjusted stubbing

        // Perform the method call
        Optional<Trainer> result = trainerDao.getByFirstNameAndLastName(firstName, lastName);

        // Verify that EntityManager.createQuery() is called with the correct query and class
        verify(entityManager, times(1)).createQuery("from Trainer t where t.firstName = :firstName and t.lastName = :lastName", Trainer.class);
        // Verify that Query.setParameter() is called with the correct parameters
        verify(typedQueryTrainer, times(1)).setParameter("firstName", firstName);
        verify(typedQueryTrainer, times(1)).setParameter("lastName", lastName);
        // Verify that Query.getResultStream() is called
        verify(typedQueryTrainer, times(1)).getResultStream();

        // Assert that the result is empty
        assertTrue(result.isEmpty());
    }
}
