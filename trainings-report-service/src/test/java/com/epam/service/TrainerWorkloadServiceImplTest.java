package com.epam.service;

import com.epam.dto.TrainerWorkloadRequestDto;
import com.epam.dto.TrainerWorkloadResponseDto;
import com.epam.mapper.TrainerWorkloadMapper;
import com.epam.model.TrainerWorkload;
import com.epam.repository.TrainerWorkloadRepository;
import com.epam.service.impl.TrainerWorkloadServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerWorkloadServiceImplTest {

    @Mock
    private TrainerWorkloadRepository trainerWorkloadRepository;

    @Mock
    private TrainerWorkloadMapper trainerWorkloadMapper;

    @InjectMocks
    private TrainerWorkloadServiceImpl service;

    @Test
    void testUpdateTrainerWorkload_NewTrainer() {
        TrainerWorkloadRequestDto dto = new TrainerWorkloadRequestDto(
            "username", "John", "Doe", true, LocalDateTime.now(), 60L, true);
        when(trainerWorkloadRepository.getByUsername(anyString())).thenReturn(Optional.empty());

        service.updateTrainerWorkload(dto);

        verify(trainerWorkloadRepository).persistTrainerWorkload(any(TrainerWorkload.class));
    }

    @Test
    void testUpdateTrainerWorkload_ExistingTrainer() {
        TrainerWorkload workload = TrainerWorkload.builder()
            .username("username")
            .firstname("John")
            .lastname("Doe")
            .isActive(true)
            .years(new ArrayList<>())
            .build();
        TrainerWorkloadRequestDto dto = new TrainerWorkloadRequestDto(
            "username", "John", "Doe", true, LocalDateTime.now(), 60L, true);
        when(trainerWorkloadRepository.getByUsername(anyString())).thenReturn(Optional.of(workload));

        service.updateTrainerWorkload(dto);

        verify(trainerWorkloadRepository).persistTrainerWorkload(workload);
    }

    @Test
    void testGetTrainerWorkload() {
        String username = "username";
        TrainerWorkload workload = TrainerWorkload.builder()
            .username(username)
            .firstname("John")
            .lastname("Doe")
            .isActive(true)
            .years(new ArrayList<>())
            .build();
        TrainerWorkloadResponseDto responseDto = new TrainerWorkloadResponseDto(username, "John", "Doe", true, new ArrayList<>());
        when(trainerWorkloadRepository.getByUsername(username)).thenReturn(Optional.of(workload));
        when(trainerWorkloadMapper.toDto(workload)).thenReturn(responseDto);

        TrainerWorkloadResponseDto result = service.getTrainerWorkload(username);

        assertEquals(responseDto, result);
        verify(trainerWorkloadMapper).toDto(workload);
    }
}