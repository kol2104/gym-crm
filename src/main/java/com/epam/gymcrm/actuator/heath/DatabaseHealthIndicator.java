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
        final String DETAIL_NAME = "database";
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(1)) {
                return Health.up().withDetail(DETAIL_NAME, "Available").build();
            }
        } catch (Exception e) {
            return Health.down(e).withDetail(DETAIL_NAME, "Not available").build();
        }
        return Health.down().withDetail(DETAIL_NAME, "Not available").build();
    }
}
