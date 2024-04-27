package com.epam.gymcrm.service;

import com.epam.gymcrm.dao.TraineeDao;
import com.epam.gymcrm.dao.TrainerDao;
import com.epam.gymcrm.dao.UserDao;
import com.epam.gymcrm.dto.trainee.TraineeForUpdateRequestDto;
import com.epam.gymcrm.dto.trainee.TraineeRequestDto;
import com.epam.gymcrm.dto.trainee.TraineeResponseDto;
import com.epam.gymcrm.dto.trainer.PlainTrainerResponseDto;
import com.epam.gymcrm.dto.trainer.TrainerUsernameDto;
import com.epam.gymcrm.dto.user.UserCredentialsDto;
import com.epam.gymcrm.exception.TraineeNotFoundException;
import com.epam.gymcrm.mapper.TraineeMapper;
import com.epam.gymcrm.mapper.TrainerMapper;
import com.epam.gymcrm.model.Trainee;
import com.epam.gymcrm.model.Trainer;
import com.epam.gymcrm.service.impl.TraineeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    @Mock
    private TrainerDao trainerDao;
    @Mock
    private UserDao userDao;
    @Mock
    private TraineeMapper traineeMapper;
    @Mock
    private TrainerMapper trainerMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private TraineeServiceImpl traineeService;


    @Test
    void testCreateTrainee() {
        TraineeRequestDto traineeRequestDto = new TraineeRequestDto(null, null, null, null);
        UserCredentialsDto userCredentialsDto = new UserCredentialsDto(null, null);
        Trainee trainee = new Trainee();
        when(traineeMapper.dtoToModel(traineeRequestDto)).thenReturn(trainee);
        when(traineeMapper.modelToCredentialsDto(trainee)).thenReturn(userCredentialsDto);
        when(traineeDao.create(trainee)).thenReturn(trainee);
        when(userDao.getByFirstNameAndLastName(null, null)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn(null);

        // When
        UserCredentialsDto createdTraineeCredentials = traineeService.create(traineeRequestDto);

        // Then
        assertNotNull(createdTraineeCredentials);
        verify(traineeMapper).dtoToModel(traineeRequestDto);
        verify(traineeMapper).modelToCredentialsDto(trainee);
        verify(traineeDao, times(1)).create(trainee);
        verify(userDao).getByFirstNameAndLastName(null, null);
        verify(passwordEncoder).encode(any());
    }

    @Test
    void testDeleteTrainee() {
        // Given
        String traineeUsername = "username";

        // When
        traineeService.deleteByUsername(traineeUsername);

        // Then
        verify(traineeDao, times(1)).deleteByUsername(traineeUsername);
    }

    @Test
    void testGetTraineeByUsername_ReturnTrainee_WhenTraineeFound() {
        // Create a trainee with a specific username for testing
        String username = "testuser";
        Trainee trainee = new Trainee();
        trainee.setUsername(username);
        TraineeResponseDto traineeResponseDto = new TraineeResponseDto(null, null, username, null, null, null, null);

        // Stub the behavior of traineeDao.getByUsername() to return the trainee
        when(traineeDao.getByUsername(username)).thenReturn(Optional.of(trainee));
        when(traineeMapper.modelToDto(trainee)).thenReturn(traineeResponseDto);

        // Call the method under test
        TraineeResponseDto retrievedTrainee = traineeService.getByUsername(username);

        // Verify that traineeDao.getByUsername() was called exactly once with the correct username
        verify(traineeDao, times(1)).getByUsername(username);

        // Verify that the correct trainee was returned
        assertNotNull(retrievedTrainee);
        assertEquals(username, retrievedTrainee.username());
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
    void testUpdateTrainee() {
        // Given
        String username = "existingUser";
        TraineeForUpdateRequestDto updateTraineeRequestDto = new TraineeForUpdateRequestDto(null, null, null, null, null, null);
        Trainee trainee = new Trainee();
        TraineeResponseDto traineeResponseDto = new TraineeResponseDto(null, null, null, null, null, null, null);
        when(traineeDao.getByUsername(username)).thenReturn(Optional.of(trainee));
        when(traineeDao.update(trainee)).thenReturn(trainee);
        when(traineeMapper.dtoToModel(updateTraineeRequestDto)).thenReturn(trainee);
        when(traineeMapper.modelToDto(trainee)).thenReturn(traineeResponseDto);

        // When
        TraineeResponseDto updatedTrainee = traineeService.update(username, updateTraineeRequestDto);

        // Then
        assertNotNull(updatedTrainee);
        assertEquals(traineeResponseDto, updatedTrainee);
        verify(traineeDao, times(1)).getByUsername(username);
        verify(traineeDao, times(1)).update(trainee);
        verify(traineeMapper, times(1)).dtoToModel(updateTraineeRequestDto);
        verify(traineeMapper, times(1)).modelToDto(trainee);
    }

    @Test
    void testUpdateTraineeNotFound() {
        // Given
        String nonExistingUsername = "nonExistingUser";
        TraineeForUpdateRequestDto updateTraineeRequestDto = new TraineeForUpdateRequestDto(null, null, null, null, null, null);
        when(traineeDao.getByUsername(nonExistingUsername)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(TraineeNotFoundException.class, () ->
                traineeService.update(nonExistingUsername, updateTraineeRequestDto));
        verify(traineeDao, times(1)).getByUsername(nonExistingUsername);
        verify(traineeDao, times(0)).update(null); // Ensure update is not called
    }

    @Test
    void testUpdateTrainersList() {
        // Prepare test data
        String existingUsername = "existingUser";
        List<TrainerUsernameDto> trainerUsernameDtos = new ArrayList<>();
        trainerUsernameDtos.add(new TrainerUsernameDto("trainer1"));
        trainerUsernameDtos.add(new TrainerUsernameDto("trainer2"));
        Trainee existingTrainee = new Trainee();
        existingTrainee.setUsername(existingUsername);

        // Mock traineeDao.getByUsername() method
        when(traineeDao.getByUsername(existingUsername)).thenReturn(Optional.of(existingTrainee));
        // Mock trainerDao.getAllByUsernames() method
        List<Trainer> trainers = new ArrayList<>();
        trainers.add(new Trainer());
        trainers.add(new Trainer());
        when(trainerDao.getAllByUsernames(List.of("trainer1", "trainer2"))).thenReturn(trainers);

        // Call the method under test
        traineeService.updateTrainersList(existingUsername, trainerUsernameDtos);

        // Verify that traineeDao.update() was called with the updated trainee
        verify(traineeDao).update(existingTrainee);

        // Verify that the trainers list is updated
        assertEquals(trainers, existingTrainee.getTrainers());
    }

    @Test
    void testUpdateTrainersListTraineeNotFound() {
        // Prepare test data
        String nonExistingUsername = "nonExistingUser";
        List<TrainerUsernameDto> trainerUsernameDtos = new ArrayList<>();
        trainerUsernameDtos.add(new TrainerUsernameDto("trainer1"));

        // Mock traineeDao.getByUsername() method to return empty optional
        when(traineeDao.getByUsername(nonExistingUsername)).thenReturn(Optional.empty());

        // Call the method under test and expect TraineeNotFoundException
        assertThrows(TraineeNotFoundException.class, () ->
                traineeService.updateTrainersList(nonExistingUsername, trainerUsernameDtos));
    }

    @Test
    void testGetUnassignedOnTraineeTrainerListByUsername() {
        // Prepare test data
        String username = "testuser";
        List<Trainer> trainers = new ArrayList<>();
        trainers.add(new Trainer(1L));
        trainers.add(new Trainer(2L));

        // Mock traineeDao.getByUsername() method
        when(traineeDao.getByUsername(username)).thenReturn(Optional.of(new Trainee()));
        // Mock traineeDao.getUnassignedOnTraineeTrainerListByUsername() method
        when(traineeDao.getUnassignedOnTraineeTrainerListByUsername(username)).thenReturn(trainers);
        // Mock trainerMapper.modelToPlainDto() method
        when(trainerMapper.modelToPlainDto(trainers.get(0))).thenReturn(new PlainTrainerResponseDto(null, null, null, null));
        when(trainerMapper.modelToPlainDto(trainers.get(1))).thenReturn(new PlainTrainerResponseDto(null, null, null, null));

        // Call the method under test
        List<PlainTrainerResponseDto> result = traineeService.getUnassignedOnTraineeTrainerListByUsername(username);

        // Verify that the returned list matches the expected list
        assertEquals(trainers.size(), result.size());
    }

    @Test
    void testActivate() {
        // Prepare test data
        String username = "testuser";
        Trainee foundTrainee = new Trainee();
        foundTrainee.setUsername(username);
        foundTrainee.setActive(false);

        // Mock traineeDao.getById() method
        when(traineeDao.getByUsername(username)).thenReturn(java.util.Optional.of(foundTrainee));

        // Call the method under test
        traineeService.activate(username);

        // Verify that trainee is activated
        assertTrue(foundTrainee.isActive());

        // Verify that traineeDao.update() method is called
        verify(traineeDao, times(1)).update(foundTrainee);
    }

    @Test
    void testActivateTraineeNotFound() {
        // Prepare test data
        String username = "testuser";

        // Mock traineeDao.getById() method to return empty optional
        when(traineeDao.getByUsername(username)).thenReturn(java.util.Optional.empty());

        // Call the method under test and verify that it throws TraineeNotFoundException
        assertThrows(TraineeNotFoundException.class, () -> traineeService.activate(username));

        // Verify that traineeDao.update() method is not called
        verify(traineeDao, never()).update(any());
    }

    @Test
    void testDeactivate() {
        // Prepare test data
        String username = "testuser";
        Trainee foundTrainee = new Trainee();
        foundTrainee.setUsername(username);
        foundTrainee.setActive(true);

        // Mock traineeDao.getById() method
        when(traineeDao.getByUsername(username)).thenReturn(java.util.Optional.of(foundTrainee));

        // Call the method under test
        traineeService.deactivate(username);

        // Verify that trainee is deactivated
        assertFalse(foundTrainee.isActive());

        // Verify that traineeDao.update() method is called
        verify(traineeDao, times(1)).update(foundTrainee);
    }

    @Test
    void testDeactivateTraineeNotFound() {
        // Prepare test data
        String username = "testuser";

        // Mock traineeDao.getById() method to return empty optional
        when(traineeDao.getByUsername(username)).thenReturn(java.util.Optional.empty());

        // Call the method under test and verify that it throws TraineeNotFoundException
        assertThrows(TraineeNotFoundException.class, () -> traineeService.deactivate(username));

        // Verify that traineeDao.update() method is not called
        verify(traineeDao, never()).update(any());
    }

/*


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



*/


}
