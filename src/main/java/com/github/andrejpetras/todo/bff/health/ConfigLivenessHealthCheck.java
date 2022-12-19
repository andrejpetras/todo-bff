package com.github.andrejpetras.todo.bff.health;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

import javax.enterprise.context.ApplicationScoped;

@Liveness
@ApplicationScoped
public class ConfigLivenessHealthCheck implements HealthCheck {

    @ConfigProperty(name = "app.config.health.liveness", defaultValue = "true")
    boolean config;

    @Override
    public HealthCheckResponse call() {
        if (config) {
            return HealthCheckResponse.up("Liveness health check");
        }
        return HealthCheckResponse.down("Liveness health check");
    }
}
