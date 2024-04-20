package com.epam.gymcrm.actuator.heath;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
@RequiredArgsConstructor
public class DatabaseHealthIndicator implements HealthIndicator {

    private final DataSource dataSource;

    @Override
    public Health health() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(1)) {
                return Health.up().withDetail("database", "Available").build();
            }
        } catch (Exception e) {
            return Health.down(e).withDetail("database", "Not available").build();
        }
        return Health.down().withDetail("database", "Not available").build();
    }
}
