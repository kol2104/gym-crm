package com.epam.gymcrm.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class TraceRequestAspect {

    @Around("@annotation(com.epam.gymcrm.aspect.annotation.TraceRequest)")
    public Object logTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        // Trace id can be passed to downstream services later
        String traceId = UUID.randomUUID().toString();
        log.info("Started request with trace id: {}", traceId);

        try {
            Object result = joinPoint.proceed();
            log.info("Trace request completed successfully");
            return result;
        } catch (Exception e) {
            log.error("Trace request failed. Trace id: {}", traceId);
            throw e;
        }
    }
}
