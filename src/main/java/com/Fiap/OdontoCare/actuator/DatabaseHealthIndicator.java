package com.Fiap.OdontoCare.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseHealthIndicator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Health health() {
        try {
            int result = jdbcTemplate.queryForObject("SELECT 1 FROM DUAL", Integer.class);
            if (result == 1) {
                return Health.up()
                        .withDetail("database", "Oracle")
                        .withDetail("status", "Conectado")
                        .build();
            } else {
                return Health.down()
                        .withDetail("database", "Oracle")
                        .withDetail("status", "Erro na consulta de verificação")
                        .build();
            }
        } catch (Exception e) {
            return Health.down()
                    .withDetail("database", "Oracle")
                    .withDetail("status", "Desconectado")
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}