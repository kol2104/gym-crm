package com.epam.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class RestCallLoggingAspect {

    private final HttpServletRequest request;

    @Around("execution(* com.epam.controller.*Controller.*(..))")
    public Object logRestCall(ProceedingJoinPoint joinPoint) throws Throwable {
        String httpMethod = request.getMethod();
        String requestUrl = request.getRequestURI();

        log.info("REST call received - Method: {}, URL: {}", httpMethod, requestUrl);

        try {
            Object result = joinPoint.proceed();
            log.info("REST call completed successfully");
            return result;
        } catch (Exception e) {
            log.error("REST call failed", e);
            throw e;
        }
    }
}