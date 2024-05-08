package com.epam.service.impl;

import com.epam.dto.TrainerWorkloadRequestDto;
import com.epam.dto.TrainerWorkloadResponseDto;
import com.epam.mapper.TrainerWorkloadMapper;
import com.epam.model.Month;
import com.epam.model.TrainerWorkload;
import com.epam.model.Year;
import com.epam.repository.TrainerWorkloadRepository;
import com.epam.service.TrainerWorkloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainerWorkloadServiceImpl implements TrainerWorkloadService {

    private final TrainerWorkloadMapper trainerWorkloadMapper;
    private final TrainerWorkloadRepository trainerWorkloadRepository;

    @Transactional
    @Override
    public void updateTrainerWorkload(TrainerWorkloadRequestDto trainerWorkloadRequestDto) {
        String username = trainerWorkloadRequestDto.username();
        boolean isSavingAction = trainerWorkloadRequestDto.action();

        Optional<TrainerWorkload> trainerWorkloadOptional = trainerWorkloadRepository.getByUsername(username);

        if (trainerWorkloadOptional.isEmpty()) {
            log.info("Update workload for new trainer");
            handleNewTrainer(trainerWorkloadRequestDto, isSavingAction);
            return;
        }
        TrainerWorkload trainerWorkload = trainerWorkloadOptional.get();
        log.info("Update workload for exist trainer");
        updateExistingTrainerWorkload(trainerWorkload, trainerWorkloadRequestDto, isSavingAction);
        trainerWorkloadRepository.persistTrainerWorkload(trainerWorkload);
    }

    private void handleNewTrainer(TrainerWorkloadRequestDto trainerWorkloadRequestDto, boolean isSavingAction) {
        TrainerWorkload trainerWorkload = buildNewTrainerWorkload(trainerWorkloadRequestDto, isSavingAction);
        trainerWorkloadRepository.persistTrainerWorkload(trainerWorkload);
    }

    private void updateExistingTrainerWorkload(TrainerWorkload workload, TrainerWorkloadRequestDto requestDto,
                                               boolean isSavingAction) {
        int yearNumber = requestDto.trainingDate().getYear();
        String monthName = requestDto.trainingDate().getMonth().name();
        if (workload.getYears() == null) {
            workload.setYears(new ArrayList<>());
        }
        Year year = findOrCreateYear(workload.getYears(), yearNumber);
        if (year.getMonths() == null) {
            year.setMonths(new ArrayList<>());
        }
        Month month = findOrCreateMonth(year.getMonths(), monthName);

        updateMonthlyDuration(month, requestDto.trainingDuration(), isSavingAction);
        if (month.getSummaryDuration() <= 0L) {
            log.debug("Delete month with empty duration");
            year.getMonths().remove(month);
            if (year.getMonths().isEmpty()) {
                log.debug("Delete year with empty months");
                workload.getYears().remove(year);
            }
        }
    }

    private Month findOrCreateMonth(List<Month> months, String monthName) {
        Optional<Month> monthOptional = months.stream()
            .filter(month -> month.getName().equals(monthName))
            .findFirst();

        if (monthOptional.isPresent()) {
            log.debug("Month found in list");
            return monthOptional.get();
        }
        Month newMonth = new Month(monthName, 0L);
        months.add(newMonth);
        log.debug("Created new month");
        return newMonth;
    }

    private Year findOrCreateYear(List<Year> years, int yearNumber) {
        Optional<Year> yearOptional = years.stream()
            .filter(year -> year.getYearNumber() == yearNumber)
            .findFirst();

        if (yearOptional.isPresent()) {
            log.debug("Year found in list");
            return yearOptional.get();
        }
        Year newYear = new Year(yearNumber, new ArrayList<>());
        years.add(newYear);
        log.debug("Created new year");
        return newYear;
    }

    private void updateMonthlyDuration(Month month, long trainingDuration, boolean isSavingAction) {
        if (isSavingAction) {
            month.setSummaryDuration(month.getSummaryDuration() + trainingDuration);
            log.debug("Duration {} added to month {}", trainingDuration, month.getName());
        } else {
            long updatedDuration = month.getSummaryDuration() - trainingDuration > 0 ?
                month.getSummaryDuration() - trainingDuration : 0L;
            month.setSummaryDuration(updatedDuration);
            log.debug("Duration {} subtracted from month {}", trainingDuration, month.getName());
        }
    }

    private TrainerWorkload buildNewTrainerWorkload(TrainerWorkloadRequestDto requestDto, boolean isSavingAction) {
        List<Year> years = new ArrayList<>();
        if (isSavingAction) {
            List<Month> months = new ArrayList<>();
            months.add(new Month(requestDto.trainingDate().getMonth().name(), requestDto.trainingDuration()));
            years.add(new Year(requestDto.trainingDate().getYear(), months));
        }
        return TrainerWorkload.builder()
            .username(requestDto.username())
            .firstname(requestDto.firstname())
            .lastname(requestDto.lastname())
            .isActive(requestDto.isActive())
            .years(years)
            .build();
    }

    @Transactional(readOnly = true)
    @Override
    public TrainerWorkloadResponseDto getTrainerWorkload(String trainerUsername) {
        log.info("Find trainer workload by trainer username");
        return trainerWorkloadMapper.toDto(trainerWorkloadRepository.getByUsername(trainerUsername).orElse(null));
    }
}
