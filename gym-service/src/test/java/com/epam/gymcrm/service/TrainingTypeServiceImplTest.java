package com.epam.gymcrm.service;

import com.epam.gymcrm.dao.TrainingTypeDao;
import com.epam.gymcrm.dto.TrainingTypeDto;
import com.epam.gymcrm.mapper.TrainingTypeMapper;
import com.epam.gymcrm.model.TrainingType;
import com.epam.gymcrm.service.impl.TrainingTypeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingTypeServiceImplTest {

    @Mock
    private TrainingTypeDao trainingTypeDao;
    @Mock
    private TrainingTypeMapper trainingTypeMapper;
    @InjectMocks
    private TrainingTypeServiceImpl trainingTypeService;

    @Test
    void testGetAll() {
        TrainingType trainingType1 = new TrainingType();
        trainingType1.setId(1L);
        trainingType1.setName("Type 1");
        TrainingType trainingType2 = new TrainingType();
        trainingType2.setId(2L);
        trainingType2.setName("Type 2");
        List<TrainingType> trainingTypes = List.of(trainingType1, trainingType2);

        // Mock behavior
        when(trainingTypeDao.getAll()).thenReturn(trainingTypes);
        when(trainingTypeMapper.modelToDto(trainingTypes.get(0))).thenReturn(new TrainingTypeDto(1L, "Type 1"));
        when(trainingTypeMapper.modelToDto(trainingTypes.get(1))).thenReturn(new TrainingTypeDto(2L, "Type 2"));

        // Call the method under test
        List<TrainingTypeDto> result = trainingTypeService.getAll();

        // Verify the result
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals("Type 1", result.get(0).name());
        assertEquals(2L, result.get(1).id());
        assertEquals("Type 2", result.get(1).name());
    }
}
