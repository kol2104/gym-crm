package com.epam.message;

import com.epam.dto.TrainerWorkloadRequestDto;
import com.epam.service.TrainerWorkloadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TrainerWorkloadJmsConsumer {

    private final TrainerWorkloadService trainerWorkloadService;

    @JmsListener(destination = "${spring.activemq.topic-name}", subscription = "consume")
    public void consumeMessage(@Valid TrainerWorkloadRequestDto trainerWorkloadRequestDto) {
        log.info("Received new message with trainer workload for trainer username: " + trainerWorkloadRequestDto.username());
        trainerWorkloadService.updateTrainerWorkload(trainerWorkloadRequestDto);
    }
}
