package com.epam.mapper;

import com.epam.dto.TrainerWorkloadResponseDto;
import com.epam.model.TrainerWorkload;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = YearMapper.class
)
public interface TrainerWorkloadMapper {
    TrainerWorkloadResponseDto toDto(TrainerWorkload trainerWorkload);
}
