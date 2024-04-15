package com.epam.gymcrm.mapper;

import com.epam.gymcrm.dto.user.UserCredentialsDto;
import com.epam.gymcrm.dto.trainee.PlainTraineeResponseDto;
import com.epam.gymcrm.dto.trainer.PlainTrainerResponseDto;
import com.epam.gymcrm.dto.trainer.TrainerForUpdateRequestDto;
import com.epam.gymcrm.dto.trainer.TrainerRequestDto;
import com.epam.gymcrm.dto.trainer.TrainerResponseDto;
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
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = TrainingTypeMapper.class
)
public interface TrainerMapper {
    UserCredentialsDto modelToCredentialsDto(Trainer trainer);
    @Mapping(target = "traineesList", source = "trainees", qualifiedByName = "traineesToPlainTraineeDto")
    @Mapping(target = "isActive", source = "active")
    TrainerResponseDto modelToDto(Trainer trainer);
    @Mapping(target = "specializationId", source = "specialization.id")
    PlainTrainerResponseDto modelToPlainDto(Trainer trainer);
    @Mapping(target = "specialization.id", source = "specializationId")
    Trainer dtoToModel(TrainerRequestDto trainerRequestDto);
    @Mapping(target = "specialization.id", source = "specializationId")
    @Mapping(target = "active", source = "isActive")
    Trainer dtoToModel(TrainerForUpdateRequestDto trainerForUpdateRequestDto);

    @Named("traineesToPlainTraineeDto")
    default List<PlainTraineeResponseDto> traineesToPlainTraineeDto(List<Trainee> trainees) {
        if (trainees == null) {
            return new ArrayList<>();
        }
        return trainees.stream()
                .map(trainee -> PlainTraineeResponseDto.builder()
                        .firstName(trainee.getFirstName())
                        .lastName(trainee.getLastName())
                        .username(trainee.getUsername())
                        .build())
                .toList();
    }
}
