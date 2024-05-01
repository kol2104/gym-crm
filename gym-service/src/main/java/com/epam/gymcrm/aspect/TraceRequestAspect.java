package com.epam.gymcrm.aspect;

import com.epam.gymcrm.common.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    @Around("@annotation(com.epam.gymcrm.aspect.annotation.TraceRequest)")
    public Object logTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        String requestHeader = request.getHeader(Constants.TRACE_HEADER.getName());
        if (requestHeader == null) {
            requestHeader = UUID.randomUUID().toString();
            request.setAttribute(Constants.TRACE_HEADER.getName(), requestHeader);
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
