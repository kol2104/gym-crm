package com.epam.gymcrm.message.impl;

import com.epam.gymcrm.dto.TrainerWorkloadRequestDto;
import com.epam.gymcrm.message.TrainingsReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TrainingsReportServiceJmsProducerImpl implements TrainingsReportService {

    private final JmsTemplate jmsTemplate;

    @Value("${spring.activemq.topic-name}")
    private String topic;

    @Override
    public void updateTrainerWorkload(TrainerWorkloadRequestDto trainerWorkloadRequestDto) {
        log.info("Sending trainer workload using JMS to '{}'", topic);
        jmsTemplate.convertAndSend(topic, trainerWorkloadRequestDto);
    }
}
