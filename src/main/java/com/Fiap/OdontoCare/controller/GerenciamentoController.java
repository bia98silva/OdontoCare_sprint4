package com.Fiap.OdontoCare.controller;

import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/gerenciamento")
public class GerenciamentoController {

    private final HealthEndpoint healthEndpoint;
    private final MetricsEndpoint metricsEndpoint;

    public GerenciamentoController(HealthEndpoint healthEndpoint, MetricsEndpoint metricsEndpoint) {
        this.healthEndpoint = healthEndpoint;
        this.metricsEndpoint = metricsEndpoint;
    }

    @GetMapping
    public String mostrarGerenciamento(Model model) {
        HealthComponent health = healthEndpoint.health();
        String statusCode = health.getStatus().getCode();
        model.addAttribute("healthStatus", statusCode);

        model.addAttribute("statusAtivo", statusCode.equals(Status.UP.getCode()));

        try {
            model.addAttribute("jvmMemory", metricsEndpoint.metric("jvm.memory.used", null));
        } catch (Exception e) {
            model.addAttribute("jvmMemoryError", "Não foi possível obter a métrica de memória: " + e.getMessage());
        }

        try {
            model.addAttribute("httpRequests", metricsEndpoint.metric("http.server.requests", null));
        } catch (Exception e) {
            model.addAttribute("httpRequestsError", "Não foi possível obter a métrica de requisições: " + e.getMessage());
        }

        return "gerenciamento";
    }
}