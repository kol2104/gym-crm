package com.epam.mapper;

import com.epam.dto.YearDto;
import com.epam.model.Year;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface YearMapper {
    YearDto toDto(Year year);
}
