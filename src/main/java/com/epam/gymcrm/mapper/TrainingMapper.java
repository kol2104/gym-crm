package com.epam.gymcrm.mapper;

import com.epam.gymcrm.dto.training.TrainingDto;
import com.epam.gymcrm.model.Training;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TrainingMapper {
    @Mapping(target = "trainingTypeName", source = "trainingType.name")
    @Mapping(target = "traineeUsername", source = "trainee.username")
    @Mapping(target = "trainerUsername", source = "trainer.username")
    TrainingDto modelToDto(Training training);

    @Mapping(target = "trainingType.name", source = "trainingTypeName")
    @Mapping(target = "trainee.username", source = "traineeUsername")
    @Mapping(target = "trainer.username", source = "trainerUsername")
    Training dtoToModel(TrainingDto trainingDto);
}
