package com.epam.gymcrm.repository;

import com.epam.gymcrm.dao.impl.UserDaoDatabase;
import com.epam.gymcrm.model.Trainer;
import com.epam.gymcrm.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDaoDatabaseTest {

    @Mock
    private EntityManager entityManager;
    @Mock
    private TypedQuery<User> typedQueryUser;
    @InjectMocks
    private UserDaoDatabase userDao;

    @Test
    void getByUsernameAndPassword_ReturnsTrainee_WhenTraineeExists() {
        // Test data
        String username = "testUser";
        String password = "testPassword";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        // Mock EntityManager.createQuery() and TypedQuery.getResultStream().findFirst()
        when(entityManager.createQuery(anyString(), eq(User.class))).thenReturn(typedQueryUser);
        when(typedQueryUser.setParameter("username", username)).thenReturn(typedQueryUser);
        when(typedQueryUser.setParameter("password", password)).thenReturn(typedQueryUser);
        when(typedQueryUser.getResultStream()).thenReturn(java.util.stream.Stream.of(user));

        // Perform the method call
        Optional<User> result = userDao.getByUsernameAndPassword(username, password);

        // Verify EntityManager.createQuery() and TypedQuery.getResultStream().findFirst() are called
        verify(entityManager, times(1)).createQuery(anyString(), eq(User.class));
        verify(typedQueryUser, times(1)).setParameter("username", username);
        verify(typedQueryUser, times(1)).setParameter("password", password);
        verify(typedQueryUser, times(1)).getResultStream();

        // Verify that the user is returned
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void getByUsernameAndPassword_ReturnsEmptyOptional_WhenTraineeDoesNotExist() {
        // Test data
        String username = "nonExistentUser";
        String password = "nonExistentPassword";

        // Mock EntityManager.createQuery() and TypedQuery.getResultStream().findFirst()
        when(entityManager.createQuery(anyString(), eq(User.class))).thenReturn(typedQueryUser);
        when(typedQueryUser.setParameter("username", username)).thenReturn(typedQueryUser);
        when(typedQueryUser.setParameter("password", password)).thenReturn(typedQueryUser);
        when(typedQueryUser.getResultStream()).thenReturn(java.util.stream.Stream.empty());

        // Perform the method call
        Optional<User> result = userDao.getByUsernameAndPassword(username, password);

        // Verify EntityManager.createQuery() and TypedQuery.getResultStream().findFirst() are called
        verify(entityManager, times(1)).createQuery(anyString(), eq(User.class));
        verify(typedQueryUser, times(1)).setParameter("username", username);
        verify(typedQueryUser, times(1)).setParameter("password", password);
        verify(typedQueryUser, times(1)).getResultStream();

        // Verify that the result is an empty Optional
        assertTrue(result.isEmpty());
    }

    @Test
    void update_ReturnsUpdatedTrainer_WhenTrainerExists() {
        // Test data
        User user = new Trainer();
        user.setId(1L);

        // Mock EntityManager.find() to return a non-null user
        when(entityManager.find(User.class, user.getId())).thenReturn(user);
        // Mock EntityManager.merge() to return the updated user
        when(entityManager.merge(user)).thenReturn(user);

        // Perform the method call
        User result = userDao.update(user);

        // Verify that EntityManager.find() is called with the correct entity class and ID
        verify(entityManager, times(1)).find(User.class, user.getId());
        // Verify that EntityManager.merge() is called with the correct user
        verify(entityManager, times(1)).merge(user);

        // Assert that the result is not null
        assertNotNull(result);
        // Assert that the result is the same as the input user
        assertEquals(user, result);
    }

    @Test
    void update_ReturnsNull_WhenTrainerDoesNotExist() {
        // Test data
        User user = new User();
        user.setId(1L);

        // Mock EntityManager.find() to return null
        when(entityManager.find(User.class, user.getId())).thenReturn(null);

        // Perform the method call
        User result = userDao.update(user);

        // Verify that EntityManager.find() is called with the correct entity class and ID
        verify(entityManager, times(1)).find(User.class, user.getId());
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
        User user = new User();

        // Mock Query.setParameter() and Query.getResultStream().findFirst() to return a non-empty Optional
        when(entityManager.createQuery(anyString(), eq(User.class))).thenReturn(typedQueryUser);
        when(typedQueryUser.setParameter(anyString(), any())).thenReturn(typedQueryUser);
        when(typedQueryUser.getResultStream()).thenReturn(Stream.of(user)); // Adjusted stubbing

        // Perform the method call
        Optional<User> result = userDao.getByFirstNameAndLastName(firstName, lastName);

        // Verify that EntityManager.createQuery() is called with the correct query and class
        verify(entityManager, times(1))
                .createQuery("from User u where u.firstName = :firstName and u.lastName = :lastName", User.class);
        // Verify that Query.setParameter() is called with the correct parameters
        verify(typedQueryUser, times(1)).setParameter("firstName", firstName);
        verify(typedQueryUser, times(1)).setParameter("lastName", lastName);
        // Verify that Query.getResultStream() is called
        verify(typedQueryUser, times(1)).getResultStream();

        // Assert that the result is not empty
        assertTrue(result.isPresent());
        // Assert that the result is the same as the input user
        assertEquals(user, result.get());
    }

    @Test
    void getByFirstNameAndLastName_ReturnsEmptyOptional_WhenTrainerDoesNotExist() {
        // Test data
        String firstName = "John";
        String lastName = "Doe";

        // Mock Query.setParameter() and Query.getResultStream().findFirst() to return an empty Optional
        when(entityManager.createQuery(anyString(), eq(User.class))).thenReturn(typedQueryUser);
        when(typedQueryUser.setParameter(anyString(), any())).thenReturn(typedQueryUser);
        when(typedQueryUser.getResultStream()).thenReturn(Stream.empty()); // Adjusted stubbing

        // Perform the method call
        Optional<User> result = userDao.getByFirstNameAndLastName(firstName, lastName);

        // Verify that EntityManager.createQuery() is called with the correct query and class
        verify(entityManager, times(1))
                .createQuery("from User u where u.firstName = :firstName and u.lastName = :lastName", User.class);
        // Verify that Query.setParameter() is called with the correct parameters
        verify(typedQueryUser, times(1)).setParameter("firstName", firstName);
        verify(typedQueryUser, times(1)).setParameter("lastName", lastName);
        // Verify that Query.getResultStream() is called
        verify(typedQueryUser, times(1)).getResultStream();

        // Assert that the result is empty
        assertTrue(result.isEmpty());
    }
}
