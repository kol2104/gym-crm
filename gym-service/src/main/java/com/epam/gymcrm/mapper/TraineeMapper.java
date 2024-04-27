package com.epam.gymcrm.mapper;

import com.epam.gymcrm.dto.user.UserCredentialsDto;
import com.epam.gymcrm.dto.trainee.TraineeRequestDto;
import com.epam.gymcrm.dto.trainee.TraineeResponseDto;
import com.epam.gymcrm.dto.trainee.TraineeForUpdateRequestDto;
import com.epam.gymcrm.dto.trainer.PlainTrainerResponseDto;
import com.epam.gymcrm.model.Trainee;
import com.epam.gymcrm.model.Trainer;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TraineeMapper {
    UserCredentialsDto modelToCredentialsDto(Trainee trainee);
    @Mapping(target = "trainersList", source = "trainers", qualifiedByName = "trainersToPlainTrainerDto")
    @Mapping(target = "isActive", source = "active")
    TraineeResponseDto modelToDto(Trainee trainee);
    Trainee dtoToModel(TraineeRequestDto traineeRequestDto);
    @Mapping(target = "active", source = "isActive")
    Trainee dtoToModel(TraineeForUpdateRequestDto updateTraineeRequestDto);

    @Named("trainersToPlainTrainerDto")
    default List<PlainTrainerResponseDto> trainersToPlainTrainerDto(List<Trainer> trainers) {
        if (trainers == null) {
            return new ArrayList<>();
        }
        return trainers.stream()
                .map(trainer -> PlainTrainerResponseDto.builder()
                        .firstName(trainer.getFirstName())
                        .lastName(trainer.getLastName())
                        .username(trainer.getUsername())
                        .specializationId(trainer.getSpecialization().getId())
                        .build())
                .toList();
    }
}
