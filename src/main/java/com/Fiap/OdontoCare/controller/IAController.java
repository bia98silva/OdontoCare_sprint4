package com.Fiap.OdontoCare.controller;

import com.Fiap.OdontoCare.entity.Consulta;
import com.Fiap.OdontoCare.entity.Paciente;
import com.Fiap.OdontoCare.service.ConsultaService;
import com.Fiap.OdontoCare.service.IAService;
import com.Fiap.OdontoCare.service.PacienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/ia")
public class IAController {

    private final IAService iaService;
    private final PacienteService pacienteService;
    private final ConsultaService consultaService;

    public IAController(
            IAService iaService,
            PacienteService pacienteService,
            ConsultaService consultaService) {
        this.iaService = iaService;
        this.pacienteService = pacienteService;
        this.consultaService = consultaService;
    }

    @GetMapping("/dashboard")
    public String dashboardIA(Model model) {
        List<Paciente> pacientes = pacienteService.findAll();
        List<Consulta> consultas = consultaService.findAll();
        
        Map<Long, Integer> consultasPorPaciente = new HashMap<>();
        Map<Long, String> nomePacientePorId = new HashMap<>();

        for (Paciente paciente : pacientes) {
            consultasPorPaciente.put(paciente.getId(), 0);
            nomePacientePorId.put(paciente.getId(), paciente.getNome());
        }

        for (Consulta consulta : consultas) {
            if (consulta.getPacienteId() != null) {
                Long pacienteId = consulta.getPacienteId();
                consultasPorPaciente.put(pacienteId, consultasPorPaciente.getOrDefault(pacienteId, 0) + 1);
            }
        }

        Map<String, List<Map<String, Object>>> pacientesPorRisco = new HashMap<>();
        pacientesPorRisco.put("alto", new ArrayList<>());
        pacientesPorRisco.put("medio", new ArrayList<>());
        pacientesPorRisco.put("baixo", new ArrayList<>());

        for (Map.Entry<Long, Integer> entry : consultasPorPaciente.entrySet()) {
            Map<String, Object> pacienteInfo = new HashMap<>();
            pacienteInfo.put("id", entry.getKey());
            pacienteInfo.put("nome", nomePacientePorId.get(entry.getKey()));
            pacienteInfo.put("consultas", entry.getValue());

            if (entry.getValue() >= 6) {
                pacientesPorRisco.get("alto").add(pacienteInfo);
            } else if (entry.getValue() >= 3) {
                pacientesPorRisco.get("medio").add(pacienteInfo);
            } else {
                pacientesPorRisco.get("baixo").add(pacienteInfo);
            }
        }

        model.addAttribute("altoRisco", pacientesPorRisco.get("alto"));
        model.addAttribute("medioRisco", pacientesPorRisco.get("medio"));
        model.addAttribute("baixoRisco", pacientesPorRisco.get("baixo"));

        return "ia/dashboard";
    }

    @GetMapping("/analise/{pacienteId}")
    public String paginaAnaliseIA(@PathVariable Long pacienteId, Model model) {
        Optional<Paciente> pacienteOpt = pacienteService.findById(pacienteId);
        if (pacienteOpt.isPresent()) {
            Paciente paciente = pacienteOpt.get();
            List<Consulta> consultas = paciente.getConsultas();

            // Determinar nível de risco
            String nivelRisco;
            if (consultas.size() >= 6) {
                nivelRisco = "alto";
            } else if (consultas.size() >= 3) {
                nivelRisco = "medio";
            } else {
                nivelRisco = "baixo";
            }

            model.addAttribute("paciente", paciente);
            model.addAttribute("consultas", consultas);
            model.addAttribute("numConsultas", consultas.size());
            model.addAttribute("nivelRisco", nivelRisco);

            return "ia/analise";
        }
        return "redirect:/ia/dashboard";
    }

    @GetMapping("/sugestoes/{pacienteId}")
    @ResponseBody
    public ResponseEntity<String> obterSugestoesTratamento(@PathVariable Long pacienteId) {
        Optional<Paciente> pacienteOpt = pacienteService.findById(pacienteId);
        if (pacienteOpt.isPresent()) {
            Paciente paciente = pacienteOpt.get();
            List<Consulta> consultas = paciente.getConsultas();

            String sugestoes = iaService.gerarSugestoesTratamento(paciente, consultas);
            return ResponseEntity.ok(sugestoes);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/analise-sintomas")
    @ResponseBody
    public ResponseEntity<String> analisarSintomas(@RequestBody String sintomas) {
        if (sintomas == null || sintomas.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("A descrição dos sintomas não pode estar vazia.");
        }

        String analise = iaService.gerarAnaliseOdontologica(sintomas);
        return ResponseEntity.ok(analise);
    }
}