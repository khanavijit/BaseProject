package org.ak.project.swagger;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class ShuttingDownHealthIndicator implements HealthIndicator {

    private boolean shuttingDown = false;

    @Override
    public Health health() {
        if (shuttingDown) {
            return Health.down()
                    .withDetail("Shutting down", true).build();
        }
        return Health.up().build();
    }

    void signalIsDown() {
        this.shuttingDown = true;
    }
}
