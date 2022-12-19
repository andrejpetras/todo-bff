package com.github.andrejpetras.todo.bff.health;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import javax.enterprise.context.ApplicationScoped;

@Readiness
@ApplicationScoped
public class ConfigRedinessHealthCheck implements HealthCheck {

    @ConfigProperty(name = "app.config.health.readiness", defaultValue = "true")
    boolean config;

    @Override
    public HealthCheckResponse call() {
        if (config) {
            return HealthCheckResponse.up("Readiness health check");
        }
        return HealthCheckResponse.down("Readiness health check");
    }
}
