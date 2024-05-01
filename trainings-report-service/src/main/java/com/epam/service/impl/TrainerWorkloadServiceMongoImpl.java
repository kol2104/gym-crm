package com.epam.service.impl;

import com.epam.dto.TrainerWorkloadRequestDto;
import com.epam.dto.TrainerWorkloadResponseDto;
import com.epam.mapper.TrainerWorkloadMapper;
import com.epam.model.TrainerWorkload;
import com.epam.model.Year;
import com.epam.service.TrainerWorkloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainerWorkloadServiceMongoImpl implements TrainerWorkloadService {

    private final MongoTemplate mongoTemplate;
    private final TrainerWorkloadMapper trainerWorkloadMapper;

    @Transactional
    @Override
    public void updateTrainerWorkload(TrainerWorkloadRequestDto trainerWorkloadRequestDto) {
        LocalDateTime trainingDate = trainerWorkloadRequestDto.trainingDate();
        int yearNumber = trainingDate.getYear();
        String monthName = trainingDate.getMonth().name();
        boolean isSavingAction = trainerWorkloadRequestDto.action();

        TrainerWorkload trainerWorkload = mongoTemplate.findById(trainerWorkloadRequestDto.username(), TrainerWorkload.class);


        if (trainerWorkload == null) {
            log.info("Update workload for new trainer");
            handleNewTrainer(trainerWorkloadRequestDto, isSavingAction);
            return;
        }
        log.info("Update workload for exist trainer");
        updateExistingTrainerWorkload(trainerWorkload, yearNumber, monthName, trainerWorkloadRequestDto, isSavingAction);
        mongoTemplate.save(trainerWorkload);
    }

    private void handleNewTrainer(TrainerWorkloadRequestDto trainerWorkloadRequestDto, boolean isSavingAction) {
        TrainerWorkload trainerWorkload = buildNewTrainerWorkload(trainerWorkloadRequestDto, isSavingAction);
        mongoTemplate.save(trainerWorkload);
    }

    private void updateExistingTrainerWorkload(TrainerWorkload workload, int yearNumber,
                                               String monthName, TrainerWorkloadRequestDto requestDto,
                                               boolean isSavingAction) {
        Year year = findOrCreateYear(workload.getYears(), yearNumber, isSavingAction);
        if (year == null) {
            return;
        }

        Map<String, Long> durations = year.getSummaryDurationByMonths();
        if (durations == null) {
            durations = new HashMap<>();
        }
        updateMonthlyDuration(durations, monthName, requestDto.trainingDuration(), isSavingAction);
        if (durations.isEmpty()) {
            log.debug("Delete year with empty map with durations");
            workload.getYears().remove(year);
        }
    }

    private Year findOrCreateYear(List<Year> years, int yearNumber, boolean isSavingAction) {
        Optional<Year> yearOptional = years.stream()
            .filter(year -> year.getYearNumber() == yearNumber)
            .findFirst();

        if (yearOptional.isPresent()) {
            log.debug("Year found in list");
            return yearOptional.get();
        } else if (isSavingAction) {
            Year newYear = Year.builder()
                .yearNumber(yearNumber)
                .summaryDurationByMonths(new HashMap<>())
                .build();
            years.add(newYear);
            log.debug("Created new year");
            return newYear;
        }
        log.debug("Not necessary to create year for NOT saving action");
        return null;
    }

    private void updateMonthlyDuration(Map<String, Long> durations, String monthName, long trainingDuration, boolean isSavingAction) {
        long currentDuration = durations.getOrDefault(monthName, 0L);

        if (isSavingAction) {
            durations.put(monthName, currentDuration + trainingDuration);
            log.debug("Duration {} added to month {}", trainingDuration, monthName);
        } else {
            long updatedDuration = currentDuration - trainingDuration;
            if (updatedDuration > 0) {
                durations.put(monthName, updatedDuration);
                log.debug("Duration {} subtracted from month {}", trainingDuration, monthName);
            } else {
                durations.remove(monthName);
                log.debug("Month {} deleted cause duration less or equal 0", monthName);
            }
        }
    }

    private TrainerWorkload buildNewTrainerWorkload(TrainerWorkloadRequestDto trainerWorkloadRequestDto, boolean isSavingAction) {
        Map<String, Long> months = new HashMap<>();
        List<Year> years = new ArrayList<>();
        if (isSavingAction) {
            months.put(trainerWorkloadRequestDto.trainingDate().getMonth().name(), trainerWorkloadRequestDto.trainingDuration());
            years.add(Year.builder()
                .yearNumber(trainerWorkloadRequestDto.trainingDate().getYear())
                .summaryDurationByMonths(months)
                .build());
        }
        return TrainerWorkload.builder()
            .username(trainerWorkloadRequestDto.username())
            .firstname(trainerWorkloadRequestDto.firstname())
            .lastname(trainerWorkloadRequestDto.lastname())
            .isActive(trainerWorkloadRequestDto.isActive())
            .years(years)
            .build();
    }

    @Transactional(readOnly = true)
    @Override
    public TrainerWorkloadResponseDto getTrainerWorkload(String trainerUsername) {
        log.info("Find trainer workload by trainer username");
        return trainerWorkloadMapper.toDto(mongoTemplate.findById(trainerUsername, TrainerWorkload.class));
    }
}
