package com.epam.aspect;

import jakarta.servlet.http.HttpServletRequest;
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

    private final HttpServletRequest request;

    @Around("@annotation(com.epam.aspect.annotation.TraceRequest)")
    public Object logTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        String requestHeader = request.getHeader("x-trace");
        if (requestHeader == null) {
            requestHeader = UUID.randomUUID().toString();
            request.setAttribute("x-trace", requestHeader);
        }
        String traceId = requestHeader;
        log.info("Started request with trace id: {}", traceId);

        try {
            Object result = joinPoint.proceed();
            log.info("Trace request with id '{}' completed successfully", traceId);
            return result;
        } catch (Exception e) {
            log.error("Trace request failed. Trace id: {}", traceId);
            throw e;
        }
    }
}
