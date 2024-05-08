package com.epam.mapper;

import com.epam.dto.MonthDto;
import com.epam.model.Month;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MonthMapper {
    MonthDto toDto(Month month);
}
