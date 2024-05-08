package com.epam.gymcrm.service.impl;

import com.epam.gymcrm.dao.TrainingTypeDao;
import com.epam.gymcrm.dto.TrainingTypeDto;
import com.epam.gymcrm.mapper.TrainingTypeMapper;
import com.epam.gymcrm.service.TrainingTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private final TrainingTypeDao trainingTypeDao;
    private final TrainingTypeMapper trainingTypeMapper;

    @Override
    public List<TrainingTypeDto> getAll() {
        log.info("Finding all training types");
        return trainingTypeDao.getAll().stream()
                .map(trainingTypeMapper::modelToDto)
                .toList();
    }
}
