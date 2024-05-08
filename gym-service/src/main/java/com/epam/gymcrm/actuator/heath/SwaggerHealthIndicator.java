package com.epam.gymcrm.actuator.heath;

import io.swagger.v3.oas.models.OpenAPI;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.core.configuration.SpringDocUIConfiguration;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SwaggerHealthIndicator implements HealthIndicator {

    private final ApplicationContext context;

    @Override
    public Health health() {
        try {
            context.getBean(OpenAPI.class);
            context.getBean(SpringDocConfiguration.class);
            context.getBean(SpringDocUIConfiguration.class);
            return Health.up().build();
        } catch (Exception e) {
            return Health.down(e).build();
        }
    }
}
