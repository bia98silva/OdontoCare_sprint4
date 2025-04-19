package com.Fiap.OdontoCare.controller;

import org.springframework.ui.Model;
import com.Fiap.OdontoCare.dto.PacienteDTO;
import com.Fiap.OdontoCare.entity.Paciente;
import com.Fiap.OdontoCare.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/pacientes")
public class PacienteViewController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping
    public String listarPacientes(Model model) {
        List<Paciente> pacientes = pacienteService.findAll();
        model.addAttribute("pacientes", pacientes);
        return "pacientes/lista";
    }

    @GetMapping("/novo")
    public String novoPacienteForm(Model model) {
        model.addAttribute("paciente", new PacienteDTO());
        return "pacientes/form";
    }

    @PostMapping("/salvar")
    public String salvarPaciente(@ModelAttribute PacienteDTO pacienteDTO) {
        pacienteService.save(pacienteDTO);
        return "redirect:/pacientes";
    }

    @GetMapping("/editar/{id}")
    public String editarPaciente(@PathVariable Long id, Model model) {
        Paciente paciente = pacienteService.findById(id).orElse(null);
        if (paciente != null) {
            PacienteDTO pacienteDTO = new PacienteDTO(paciente.getId(), paciente.getNome(), paciente.getDataNascimento(),
                    paciente.getCpf(), paciente.getCarteirinha(), paciente.getTelefone(),
                    paciente.getEmail(), paciente.getEndereco());
            model.addAttribute("paciente", pacienteDTO);
        }
        return "pacientes/form";
    }

    @GetMapping("/deletar/{id}")
    public String deletarPaciente(@PathVariable Long id) {
        pacienteService.deleteById(id);
        return "redirect:/pacientes";
    }
}
