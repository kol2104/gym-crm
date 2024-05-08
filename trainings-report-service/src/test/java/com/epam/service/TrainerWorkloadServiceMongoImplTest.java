package com.epam.service;

import com.epam.dto.TrainerWorkloadRequestDto;
import com.epam.dto.TrainerWorkloadResponseDto;
import com.epam.mapper.TrainerWorkloadMapper;
import com.epam.model.TrainerWorkload;
import com.epam.service.impl.TrainerWorkloadServiceMongoImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerWorkloadServiceMongoImplTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private TrainerWorkloadMapper trainerWorkloadMapper;

    @InjectMocks
    private TrainerWorkloadServiceMongoImpl service;

    @Test
    void testUpdateTrainerWorkload_NewTrainer() {
        TrainerWorkloadRequestDto dto = new TrainerWorkloadRequestDto(
            "username", "John", "Doe", true, LocalDateTime.now(), 60L, true);
        when(mongoTemplate.findById(anyString(), eq(TrainerWorkload.class))).thenReturn(null);

        service.updateTrainerWorkload(dto);

        verify(mongoTemplate).save(any(TrainerWorkload.class));
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
        when(mongoTemplate.findById(anyString(), eq(TrainerWorkload.class))).thenReturn(workload);

        service.updateTrainerWorkload(dto);

        verify(mongoTemplate).save(workload);
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
        when(mongoTemplate.findById(username, TrainerWorkload.class)).thenReturn(workload);
        when(trainerWorkloadMapper.toDto(workload)).thenReturn(responseDto);

        TrainerWorkloadResponseDto result = service.getTrainerWorkload(username);

        assertEquals(responseDto, result);
        verify(trainerWorkloadMapper).toDto(workload);
    }
}