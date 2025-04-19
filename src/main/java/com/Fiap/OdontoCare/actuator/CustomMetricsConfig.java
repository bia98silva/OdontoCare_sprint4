package com.Fiap.OdontoCare.actuator;

import com.Fiap.OdontoCare.repository.ConsultaRepository;
import com.Fiap.OdontoCare.repository.DentistaRepository;
import com.Fiap.OdontoCare.repository.PacienteRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomMetricsConfig {

    private final PacienteRepository pacienteRepository;
    private final DentistaRepository dentistaRepository;
    private final ConsultaRepository consultaRepository;

    public CustomMetricsConfig(
            PacienteRepository pacienteRepository,
            DentistaRepository dentistaRepository,
            ConsultaRepository consultaRepository) {
        this.pacienteRepository = pacienteRepository;
        this.dentistaRepository = dentistaRepository;
        this.consultaRepository = consultaRepository;
    }

    @Bean
    public Gauge pacientesGauge(MeterRegistry registry) {
        return Gauge.builder("odonto.pacientes.count", pacienteRepository::count)
                .description("Número total de pacientes cadastrados")
                .register(registry);
    }

    @Bean
    public Gauge dentistasGauge(MeterRegistry registry) {
        return Gauge.builder("odonto.dentistas.count", dentistaRepository::count)
                .description("Número total de dentistas cadastrados")
                .register(registry);
    }

    @Bean
    public Gauge consultasGauge(MeterRegistry registry) {
        return Gauge.builder("odonto.consultas.count", consultaRepository::count)
                .description("Número total de consultas agendadas")
                .register(registry);
    }
}