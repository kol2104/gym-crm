package com.epam.gymcrm.service;

import com.epam.gymcrm.dao.TraineeDao;
import com.epam.gymcrm.exception.TraineeNotFoundException;
import com.epam.gymcrm.model.Trainee;
import com.epam.gymcrm.model.Trainer;
import com.epam.gymcrm.service.impl.TraineeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
    void testCreateTrainee() {
        // Given
        Trainee trainee = new Trainee();
        when(traineeDao.create(trainee)).thenReturn(trainee);

        // When
        Trainee createdTrainee = traineeService.create(trainee);

        // Then
        assertNotNull(createdTrainee);
        assertEquals(trainee, createdTrainee);
        verify(traineeDao, times(1)).create(trainee);
    }

    @Test
    void testGetAllTrainees() {
        // Given
        List<Trainee> expectedTrainees = Arrays.asList(new Trainee(), new Trainee());
        when(traineeDao.getAll()).thenReturn(expectedTrainees);

        // When
        List<Trainee> actualTrainees = traineeService.getAll();

        // Then
        assertNotNull(actualTrainees);
        assertEquals(expectedTrainees.size(), actualTrainees.size());
        assertTrue(actualTrainees.containsAll(expectedTrainees));
        verify(traineeDao, times(1)).getAll();
    }

    @Test
    void testGetByIdTraineeFound() {
        // Given
        long traineeId = 1L;
        Trainee trainee = new Trainee();
        when(traineeDao.getById(traineeId)).thenReturn(Optional.of(trainee));

        // When
        Trainee foundTrainee = traineeService.getById(traineeId);

        // Then
        assertNotNull(foundTrainee);
        assertEquals(trainee, foundTrainee);
        verify(traineeDao, times(1)).getById(traineeId);
    }

    @Test
    void testGetByIdTraineeNotFound() {
        // Given
        long traineeId = 1L;
        when(traineeDao.getById(traineeId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(TraineeNotFoundException.class, () -> traineeService.getById(traineeId));
        verify(traineeDao, times(1)).getById(traineeId);
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
        when(traineeDao.getById(traineeId)).thenReturn(Optional.of(trainee));
        when(traineeDao.update(trainee)).thenReturn(trainee);

        // When
        Trainee updatedTrainee = traineeService.update(traineeId, trainee);

        // Then
        assertNotNull(updatedTrainee);
        assertEquals(trainee, updatedTrainee);
        verify(traineeDao, times(1)).getById(traineeId);
        verify(traineeDao, times(1)).update(trainee);
    }

    @Test
    void testUpdateTraineeNotFound() {
        // Given
        long traineeId = 1L;
        Trainee trainee = new Trainee();
        when(traineeDao.getById(traineeId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(TraineeNotFoundException.class, () -> traineeService.update(traineeId, trainee));
        verify(traineeDao, times(1)).getById(traineeId);
        verify(traineeDao, never()).update(trainee);
    }

    @Test
    void testGetTraineeByUsername_ReturnTrainee_WhenTraineeFound() {
        // Create a trainee with a specific username for testing
        String username = "testuser";
        Trainee trainee = new Trainee();
        trainee.setUsername(username);

        // Stub the behavior of traineeDao.getByUsername() to return the trainee
        when(traineeDao.getByUsername(username)).thenReturn(Optional.of(trainee));

        // Call the method under test
        Trainee retrievedTrainee = traineeService.getByUsername(username);

        // Verify that traineeDao.getByUsername() was called exactly once with the correct username
        verify(traineeDao, times(1)).getByUsername(username);

        // Verify that the correct trainee was returned
        assertNotNull(retrievedTrainee);
        assertEquals(username, retrievedTrainee.getUsername());
    }

    @Test
    void testGetTraineeByUsername_ThrowException_WhenTraineeNotFound() {
        // Specify a non-existing username for testing
        String nonExistingUsername = "nonexistinguser";

        // Stub the behavior of traineeDao.getByUsername() to return an empty Optional
        when(traineeDao.getByUsername(nonExistingUsername)).thenReturn(Optional.empty());

        // Call the method under test and verify that it throws TraineeNotFoundException
        assertThrows(TraineeNotFoundException.class, () -> traineeService.getByUsername(nonExistingUsername));

        // Verify that traineeDao.getByUsername() was called exactly once with the correct username
        verify(traineeDao, times(1)).getByUsername(nonExistingUsername);
    }

    @Test
    void testIsValidUsernameAndPassword_ReturnTrue_WhenTraineeFound() {
        // Define valid credentials for testing
        String username = "testuser";
        String password = "testpassword";

        // Create a trainee with the provided username and password
        Trainee trainee = new Trainee();
        trainee.setUsername(username);
        trainee.setPassword(password);

        // Stub the behavior of traineeDao.getByUsernameAndPassword() to return the trainee
        when(traineeDao.getByUsernameAndPassword(username, password)).thenReturn(Optional.of(trainee));

        // Call the method under test and verify that it returns true
        assertTrue(traineeService.isUsernameAndPasswordValid(username, password));

        // Verify that traineeDao.getByUsernameAndPassword() was called exactly once with the correct credentials
        verify(traineeDao, times(1)).getByUsernameAndPassword(username, password);
    }

    @Test
    void testIsValidUsernameAndPassword_ReturnFalse_WhenTraineeNotFound() {
        // Define invalid credentials for testing
        String invalidUsername = "invaliduser";
        String invalidPassword = "invalidpassword";

        // Stub the behavior of traineeDao.getByUsernameAndPassword() to return an empty Optional
        when(traineeDao.getByUsernameAndPassword(invalidUsername, invalidPassword)).thenReturn(Optional.empty());

        // Call the method under test and verify that it returns false
        assertFalse(traineeService.isUsernameAndPasswordValid(invalidUsername, invalidPassword));

        // Verify that traineeDao.getByUsernameAndPassword() was called exactly once with the correct credentials
        verify(traineeDao, times(1)).getByUsernameAndPassword(invalidUsername, invalidPassword);
    }

    @Test
    void testDeleteTraineeById() {
        // Define a trainee ID for testing
        Long traineeId = 1L;

        // Call the method under test
        traineeService.delete(traineeId);

        // Verify that traineeDao.delete() was called exactly once with the correct ID
        verify(traineeDao).delete(traineeId);
    }

    @Test
    void testDeleteTraineeByUsername() {
        // Define a trainee username for testing
        String traineeUsername = "testUser";

        // Call the method under test
        traineeService.deleteByUsername(traineeUsername);

        // Verify that traineeDao.deleteByUsername() was called exactly once with the correct username
        verify(traineeDao).deleteByUsername(traineeUsername);
    }

    @Test
    void testUpdateTraineeWithExistingId() {
        // Prepare test data
        Long existingId = 1L;
        Trainee existingTrainee = new Trainee();
        existingTrainee.setId(existingId);

        // Mock traineeDao.getById() method
        when(traineeDao.getById(existingId)).thenReturn(Optional.of(existingTrainee));

        // Mock traineeDao.update() method to return the updated trainee
        when(traineeDao.update(any(Trainee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call the method under test
        Trainee updatedTrainee = new Trainee();
        updatedTrainee.setFirstName("Jane");
        updatedTrainee.setLastName("Doe");
        Trainee returnedTrainee = traineeService.update(existingId, updatedTrainee);

        // Verify that traineeDao.update() was called with the updated trainee
        verify(traineeDao).update(updatedTrainee);

        // Verify that the returned trainee matches the updated trainee
        assertNotNull(returnedTrainee);
        assertEquals(existingId, returnedTrainee.getId());
        assertEquals("Jane", returnedTrainee.getFirstName());
        assertEquals("Doe", returnedTrainee.getLastName());
    }

    @Test
    void testUpdateTraineeWithNonExistingId() {
        // Prepare test data
        Long nonExistingId = 2L;

        // Mock traineeDao.getById() method to return empty Optional
        when(traineeDao.getById(nonExistingId)).thenReturn(Optional.empty());

        // Call the method under test and expect TraineeNotFoundException
        assertThrows(TraineeNotFoundException.class, () -> traineeService.update(nonExistingId, null));
    }

    @Test
    void testUpdatePassword() {
        // Prepare test data
        Long existingId = 1L;
        String newPassword = "newPassword";
        Trainee existingTrainee = new Trainee();
        existingTrainee.setId(existingId);
        existingTrainee.setPassword("oldPassword");

        // Mock traineeDao.getById() method
        when(traineeDao.getById(existingId)).thenReturn(Optional.of(existingTrainee));

        // Call the method under test
        traineeService.updatePassword(existingId, newPassword);

        // Verify that traineeDao.update() was called with the updated trainee
        verify(traineeDao).update(existingTrainee);

        // Verify that the password is updated
        assertEquals(newPassword, existingTrainee.getPassword());
    }

    @Test
    void testUpdatePasswordWithNullPassword() {
        // Prepare test data
        Long existingId = 1L;
        Trainee existingTrainee = new Trainee();
        existingTrainee.setId(existingId);

        // Mock traineeDao.getById() method
        when(traineeDao.getById(existingId)).thenReturn(Optional.of(existingTrainee));

        // Call the method under test with null password
        traineeService.updatePassword(existingId, null);

        // Verify that traineeDao.update() was called with the trainee
        verify(traineeDao).update(existingTrainee);

        // Verify that the password remains unchanged
        assertNull(existingTrainee.getPassword());
    }

    @Test
    void testUpdatePasswordTraineeNotFound() {
        // Prepare test data
        Long nonExistingId = 100L;

        // Mock traineeDao.getById() method to return empty optional
        when(traineeDao.getById(nonExistingId)).thenReturn(Optional.empty());

        // Call the method under test and expect TraineeNotFoundException
        assertThrows(TraineeNotFoundException.class, () -> traineeService.updatePassword(nonExistingId, "password"));
    }

    @Test
    void testUpdateTrainersList() {
        // Prepare test data
        Long existingId = 1L;
        List<Trainer> trainers = new ArrayList<>();
        trainers.add(new Trainer(1L));
        trainers.add(new Trainer(2L));
        Trainee existingTrainee = new Trainee();
        existingTrainee.setId(existingId);

        // Mock traineeDao.getById() method
        when(traineeDao.getById(existingId)).thenReturn(Optional.of(existingTrainee));

        // Call the method under test
        traineeService.updateTrainersList(existingId, trainers);

        // Verify that traineeDao.update() was called with the updated trainee
        verify(traineeDao).update(existingTrainee);

        // Verify that the trainers list is updated
        assertEquals(trainers, existingTrainee.getTrainers());
    }

    @Test
    void testUpdateTrainersListTraineeNotFound() {
        // Prepare test data
        Long nonExistingId = 100L;
        List<Trainer> trainers = new ArrayList<>();
        trainers.add(new Trainer(1L));

        // Mock traineeDao.getById() method to return empty optional
        when(traineeDao.getById(nonExistingId)).thenReturn(Optional.empty());

        // Call the method under test and expect TraineeNotFoundException
        assertThrows(TraineeNotFoundException.class, () -> traineeService.updateTrainersList(nonExistingId, trainers));
    }

    @Test
    void testGetUnassignedOnTraineeTrainerListByUsername() {
        // Prepare test data
        String username = "testuser";
        Trainee trainee = new Trainee();
        trainee.setUsername(username);
        List<Trainer> trainers = new ArrayList<>();
        trainers.add(new Trainer(1L));
        trainers.add(new Trainer(2L));

        // Mock traineeDao.getUnassignedOnTraineeTrainerListByUsername() method
        when(traineeDao.getUnassignedOnTraineeTrainerListByUsername(username)).thenReturn(trainers);

        // Call the method under test
        List<Trainer> result = traineeService.getUnassignedOnTraineeTrainerListByUsername(username);

        // Verify that the returned list matches the expected list
        assertEquals(trainers, result);
    }

    @Test
    void testActivate() {
        // Prepare test data
        long traineeId = 1;
        Trainee foundTrainee = new Trainee();
        foundTrainee.setId(traineeId);
        foundTrainee.setActive(false);

        // Mock traineeDao.getById() method
        when(traineeDao.getById(traineeId)).thenReturn(java.util.Optional.of(foundTrainee));

        // Call the method under test
        traineeService.activate(traineeId);

        // Verify that trainee is activated
        assertTrue(foundTrainee.isActive());

        // Verify that traineeDao.update() method is called
        verify(traineeDao, times(1)).update(foundTrainee);
    }

    @Test
    void testActivateTraineeNotFound() {
        // Prepare test data
        long traineeId = 1;

        // Mock traineeDao.getById() method to return empty optional
        when(traineeDao.getById(traineeId)).thenReturn(java.util.Optional.empty());

        // Call the method under test and verify that it throws TraineeNotFoundException
        assertThrows(TraineeNotFoundException.class, () -> traineeService.activate(traineeId));

        // Verify that traineeDao.update() method is not called
        verify(traineeDao, never()).update(any());
    }

    @Test
    void testDeactivate() {
        // Prepare test data
        long traineeId = 1;
        Trainee foundTrainee = new Trainee();
        foundTrainee.setId(traineeId);
        foundTrainee.setActive(true);

        // Mock traineeDao.getById() method
        when(traineeDao.getById(traineeId)).thenReturn(java.util.Optional.of(foundTrainee));

        // Call the method under test
        traineeService.deactivate(traineeId);

        // Verify that trainee is deactivated
        assertFalse(foundTrainee.isActive());

        // Verify that traineeDao.update() method is called
        verify(traineeDao, times(1)).update(foundTrainee);
    }

    @Test
    void testDeactivateTraineeNotFound() {
        // Prepare test data
        long traineeId = 1;

        // Mock traineeDao.getById() method to return empty optional
        when(traineeDao.getById(traineeId)).thenReturn(java.util.Optional.empty());

        // Call the method under test and verify that it throws TraineeNotFoundException
        assertThrows(TraineeNotFoundException.class, () -> traineeService.deactivate(traineeId));

        // Verify that traineeDao.update() method is not called
        verify(traineeDao, never()).update(any());
    }




}
