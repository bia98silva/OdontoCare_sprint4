package com.Fiap.OdontoCare.controller;

import com.Fiap.OdontoCare.entity.Consulta;
import com.Fiap.OdontoCare.service.ConsultaService;
import com.Fiap.OdontoCare.service.DentistaService;
import com.Fiap.OdontoCare.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final PacienteService pacienteService;
    private final DentistaService dentistaService;
    private final ConsultaService consultaService;

    public  DashboardController(
            PacienteService pacienteService,
            DentistaService dentistaService,
            ConsultaService consultaService) {
        this.pacienteService = pacienteService;
        this.dentistaService = dentistaService;
        this.consultaService = consultaService;
    }

    @GetMapping
    public String mostrarDashboard(Model model) {
        model.addAttribute("totalPacientes", pacienteService.count());
        model.addAttribute("totalDentistas", dentistaService.count());
        model.addAttribute("totalConsultas", consultaService.count());

        List<Consulta> consultas = consultaService.findAll();
        model.addAttribute("consultas", consultas);

        return "dashboard";
    }

    @PostMapping("/excluir/{id}")
    public String excluirConsulta(@PathVariable Long id) {
        consultaService.deleteById(id);
        return "redirect:/dashboard";
    }
}
