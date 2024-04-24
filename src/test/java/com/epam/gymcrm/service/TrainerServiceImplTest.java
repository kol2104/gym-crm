package com.epam.gymcrm.service;

import com.epam.gymcrm.dao.TrainerDao;
import com.epam.gymcrm.dao.TrainingTypeDao;
import com.epam.gymcrm.dao.UserDao;
import com.epam.gymcrm.dto.trainer.TrainerForUpdateRequestDto;
import com.epam.gymcrm.dto.trainer.TrainerRequestDto;
import com.epam.gymcrm.dto.trainer.TrainerResponseDto;
import com.epam.gymcrm.dto.user.UserCredentialsDto;
import com.epam.gymcrm.exception.TrainerNotFoundException;
import com.epam.gymcrm.mapper.TrainerMapper;
import com.epam.gymcrm.model.Trainer;
import com.epam.gymcrm.model.TrainingType;
import com.epam.gymcrm.service.impl.TrainerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
class TrainerServiceImplTest {

    @Mock
    private TrainerDao trainerDao;
    @Mock
    private UserDao userDao;
    @Mock
    private TrainingTypeDao trainingTypeDao;
    @Mock
    private TrainerMapper trainerMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @Test
    void testCreateTrainer() {
        TrainerRequestDto trainerRequestDto = new TrainerRequestDto(null, null, null);
        UserCredentialsDto userCredentialsDto = new UserCredentialsDto(null, null);
        Trainer trainer = new Trainer();
        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);
        trainer.setSpecialization(trainingType);
        when(trainerMapper.dtoToModel(trainerRequestDto)).thenReturn(trainer);
        when(trainerMapper.modelToCredentialsDto(trainer)).thenReturn(userCredentialsDto);
        when(trainerDao.create(trainer)).thenReturn(trainer);
        when(userDao.getByFirstNameAndLastName(null, null)).thenReturn(Optional.empty());
        when(trainingTypeDao.getById(1L)).thenReturn(Optional.of(trainingType));
        when(passwordEncoder.encode(any())).thenReturn(null);

        // When
        UserCredentialsDto createdTraineeCredentials = trainerService.create(trainerRequestDto);

        // Then
        assertNotNull(createdTraineeCredentials);
        verify(trainerMapper).dtoToModel(trainerRequestDto);
        verify(trainerMapper).modelToCredentialsDto(trainer);
        verify(trainerDao, times(1)).create(trainer);
        verify(userDao).getByFirstNameAndLastName(null, null);
        verify(trainingTypeDao).getById(1L);
        verify(passwordEncoder).encode(any());
    }

    @Test
    void testGetTrainerByUsername() {
        String username = "john.doe";
        Trainer trainer = new Trainer();
        trainer.setUsername(username);
        TrainerResponseDto trainerResponseDto = new TrainerResponseDto(null,null, username, null, null, null);

        when(trainerDao.getByUsername(username)).thenReturn(Optional.of(trainer));
        when(trainerMapper.modelToDto(trainer)).thenReturn(trainerResponseDto);

        TrainerResponseDto foundTrainer = trainerService.getByUsername(username);

        assertNotNull(foundTrainer);
        assertEquals(username, foundTrainer.username());
        verify(trainerDao, times(1)).getByUsername(username);
    }

    @Test
    void testGetTrainerByNonExistingUsername() {
        String nonExistingUsername = "non_existing_username";

        when(trainerDao.getByUsername(nonExistingUsername)).thenReturn(Optional.empty());

        TrainerNotFoundException exception = assertThrows(TrainerNotFoundException.class, () ->
                trainerService.getByUsername(nonExistingUsername)
        );

        assertEquals("Trainer with username '" + nonExistingUsername + "' not found", exception.getMessage());
        verify(trainerDao, times(1)).getByUsername(nonExistingUsername);
    }

    @Test
    void testUpdateTrainer() {
        // Given
        String username = "existingUser";
        TrainerForUpdateRequestDto updateTrainerRequestDto = new TrainerForUpdateRequestDto( null, null, null, null, null);
        Trainer trainer = new Trainer();
        TrainerResponseDto trainerResponseDto = new TrainerResponseDto(null, null, null, null, null, null);
        when(trainerDao.getByUsername(username)).thenReturn(Optional.of(trainer));
        when(trainerDao.update(trainer)).thenReturn(trainer);
        when(trainerMapper.dtoToModel(updateTrainerRequestDto)).thenReturn(trainer);
        when(trainerMapper.modelToDto(trainer)).thenReturn(trainerResponseDto);
        when(trainingTypeDao.getById(any())).thenReturn(Optional.of(new TrainingType()));

        // When
        TrainerResponseDto updatedTrainee = trainerService.update(username, updateTrainerRequestDto);

        // Then
        assertNotNull(updatedTrainee);
        assertEquals(trainerResponseDto, updatedTrainee);
        verify(trainerDao, times(1)).getByUsername(username);
        verify(trainerDao, times(1)).update(trainer);
        verify(trainerMapper, times(1)).dtoToModel(updateTrainerRequestDto);
        verify(trainerMapper, times(1)).modelToDto(trainer);
    }

    @Test
    void testUpdateTrainerNotFound() {
        // Given
        String username = "user";
        Trainer trainer = new Trainer();
        TrainerForUpdateRequestDto updateTrainerRequestDto = new TrainerForUpdateRequestDto( null, null, null, null, null);
        when(trainerDao.getByUsername(username)).thenReturn(Optional.empty());
        when(trainerMapper.dtoToModel(updateTrainerRequestDto)).thenReturn(trainer);

        // When/Then
        assertThrows(TrainerNotFoundException.class, () -> trainerService.update(username, updateTrainerRequestDto));
        verify(trainerDao, times(1)).getByUsername(username);
        verify(trainerMapper, times(1)).dtoToModel(updateTrainerRequestDto);
        verify(trainerDao, never()).update(trainer);
    }


    @Test
    void testActivateExistingTrainer() {
        String username = "username";
        Trainer trainer = new Trainer();
        trainer.setUsername(username);

        when(trainerDao.getByUsername(username)).thenReturn(Optional.of(trainer));

        assertDoesNotThrow(() -> trainerService.activate(username));
        assertTrue(trainer.isActive());
        verify(trainerDao, times(1)).getByUsername(username);
        verify(trainerDao, times(1)).update(trainer);
    }

    @Test
    void testActivateNonExistingTrainer() {
        String username = "username";

        when(trainerDao.getByUsername(username)).thenReturn(Optional.empty());

        assertThrows(TrainerNotFoundException.class, () -> trainerService.activate(username));
        verify(trainerDao, times(1)).getByUsername(username);
        verify(trainerDao, never()).update(any());
    }

    @Test
    void testActivateAlreadyActivatedTrainer() {
        String username = "username";
        Trainer trainer = new Trainer();
        trainer.setUsername(username);
        trainer.setActive(true);

        when(trainerDao.getByUsername(username)).thenReturn(Optional.of(trainer));

        assertDoesNotThrow(() -> trainerService.activate(username));
        assertTrue(trainer.isActive()); // Ensure active status remains true
        verify(trainerDao, times(1)).getByUsername(username);
        verify(trainerDao, never()).update(any());
    }

    @Test
    void testDeactivateExistingTrainer() {
        String username = "username";
        Trainer trainer = new Trainer();
        trainer.setUsername(username);
        trainer.setActive(true);

        when(trainerDao.getByUsername(username)).thenReturn(Optional.of(trainer));

        assertDoesNotThrow(() -> trainerService.deactivate(username));
        assertFalse(trainer.isActive());
        verify(trainerDao, times(1)).getByUsername(username);
        verify(trainerDao, times(1)).update(trainer);
    }

    @Test
    void testDeactivateNonExistingTrainer() {
        String username = "username";

        when(trainerDao.getByUsername(username)).thenReturn(Optional.empty());

        assertThrows(TrainerNotFoundException.class, () -> trainerService.deactivate(username));
        verify(trainerDao, times(1)).getByUsername(username);
        verify(trainerDao, never()).update(any());
    }

    @Test
    void testDeactivateAlreadyDeactivatedTrainer() {
        String username = "username";
        Trainer trainer = new Trainer();
        trainer.setUsername(username);
        trainer.setActive(false);

        when(trainerDao.getByUsername(username)).thenReturn(Optional.of(trainer));

        assertDoesNotThrow(() -> trainerService.deactivate(username));
        assertFalse(trainer.isActive()); // Ensure active status remains false
        verify(trainerDao, times(1)).getByUsername(username);
        verify(trainerDao, never()).update(any());
    }
/*


    @Test
    void testIsUsernameAndPasswordValidWithValidCredentials() {
        String username = "john.doe";
        String password = "password";
        Trainer trainer = new Trainer();
        trainer.setUsername(username);
        trainer.setPassword(password);

        when(trainerDao.getByUsernameAndPassword(username, password)).thenReturn(Optional.of(trainer));

        boolean isValid = trainerService.isUsernameAndPasswordValid(username, password);

        assertTrue(isValid);
        verify(trainerDao, times(1)).getByUsernameAndPassword(username, password);
    }

    @Test
    void testIsUsernameAndPasswordValidWithInvalidCredentials() {
        String username = "john.doe";
        String password = "password";

        when(trainerDao.getByUsernameAndPassword(username, password)).thenReturn(Optional.empty());

        boolean isValid = trainerService.isUsernameAndPasswordValid(username, password);

        assertFalse(isValid);
        verify(trainerDao, times(1)).getByUsernameAndPassword(username, password);
    }

    @Test
    void testUpdateTrainer() {
        // Given
        long trainerId = 1L;
        Trainer trainer = new Trainer();
        when(trainerDao.getById(trainerId)).thenReturn(Optional.of(trainer));
        when(trainerDao.update(trainer)).thenReturn(trainer);

        // When
        Trainer updatedTrainer = trainerService.update(trainerId, trainer);

        // Then
        assertNotNull(updatedTrainer);
        assertEquals(trainer, updatedTrainer);
        verify(trainerDao, times(1)).getById(trainerId);
        verify(trainerDao, times(1)).update(trainer);
    }

    @Test
    void testUpdateTrainerNotFound() {
        // Given
        long trainerId = 1L;
        Trainer trainer = new Trainer();
        when(trainerDao.getById(trainerId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(TrainerNotFoundException.class, () -> trainerService.update(trainerId, trainer));
        verify(trainerDao, times(1)).getById(trainerId);
        verify(trainerDao, never()).update(trainer);
    }

    @Test
    void testUpdatePasswordWithNonNullPassword() {
        Long id = 1L;
        String newPassword = "newPassword";
        Trainer trainer = new Trainer();
        trainer.setId(id);

        when(trainerDao.getById(id)).thenReturn(Optional.of(trainer));

        assertDoesNotThrow(() -> trainerService.updatePassword(id, newPassword));
        assertEquals(newPassword, trainer.getPassword());
        verify(trainerDao, times(1)).getById(id);
        verify(trainerDao, times(1)).update(trainer);
    }

    @Test
    void testUpdatePasswordWithNullPassword() {
        Long id = 1L;
        Trainer trainer = new Trainer();
        trainer.setId(id);

        when(trainerDao.getById(id)).thenReturn(Optional.of(trainer));

        assertDoesNotThrow(() -> trainerService.updatePassword(id, null));
        assertNull(trainer.getPassword());
        verify(trainerDao, times(1)).getById(id);
        verify(trainerDao, times(1)).update(trainer);
    }

    @Test
    void testUpdatePasswordWithNonExistingTrainer() {
        Long id = 1L;

        when(trainerDao.getById(id)).thenReturn(Optional.empty());

        assertThrows(TrainerNotFoundException.class, () -> trainerService.updatePassword(id, "newPassword"));
        verify(trainerDao, times(1)).getById(id);
        verify(trainerDao, never()).update(any());
    }
*/
}
