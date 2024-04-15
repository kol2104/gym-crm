package com.epam.gymcrm.mapper;

import com.epam.gymcrm.dto.TrainingTypeDto;
import com.epam.gymcrm.model.TrainingType;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TrainingTypeMapper {
    TrainingTypeDto modelToDto(TrainingType trainingType);
}
