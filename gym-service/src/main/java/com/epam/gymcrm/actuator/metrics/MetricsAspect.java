package com.epam.gymcrm.actuator.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class MetricsAspect {

    private final MeterRegistry meterRegistry;

    @Pointcut("execution(* com.epam.gymcrm..*(..)) && !execution(* com.epam.gymcrm.auth..*(..))")
    public void monitor() {}

    @Before("monitor()")
    public void countMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringType().getName();
        Counter counter = Counter.builder("method.calls")
                .tag("method", methodName)
                .tag("class", className)
                .description("Number of calls for " + methodName)
                .register(meterRegistry);
        counter.increment();
    }

    @Around("monitor()")
    public Object timeMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringType().getName();
        Timer timer = Timer.builder("method.execution.time")
                .tag("method", methodName)
                .tag("class", className)
                .description("Execution time for " + methodName)
                .register(meterRegistry);

        return timer.recordCallable(() -> {
            try {
                return joinPoint.proceed();
            } catch (RuntimeException e) {
                throw e;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }
}
