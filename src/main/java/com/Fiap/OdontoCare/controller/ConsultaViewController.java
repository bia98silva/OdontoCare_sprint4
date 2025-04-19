package com.Fiap.OdontoCare.controller;

import com.Fiap.OdontoCare.dto.ConsultaDTO;
import com.Fiap.OdontoCare.entity.Consulta;
import com.Fiap.OdontoCare.entity.Dentista;
import com.Fiap.OdontoCare.entity.Paciente;
import com.Fiap.OdontoCare.service.ConsultaService;
import com.Fiap.OdontoCare.service.DentistaService;
import com.Fiap.OdontoCare.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/consultasView")
public class ConsultaViewController {

    private final ConsultaService consultaService;
    private final PacienteService pacienteService;
    private final DentistaService dentistaService;

    public ConsultaViewController(
            ConsultaService consultaService,
            PacienteService pacienteService,
            DentistaService dentistaService) {
        this.consultaService = consultaService;
        this.pacienteService = pacienteService;
        this.dentistaService = dentistaService;
    }
    @GetMapping
    public String listarConsultas(Model model) {
        model.addAttribute("consultas", consultaService.findAll());
        return "consultaslista";
    }

    @GetMapping("/nova")
    public String novaConsulta(Model model) {
        model.addAttribute("pacientes", pacienteService.findAll());  // Lista os pacientes cadastrados
        model.addAttribute("dentistas", dentistaService.findAll());  // Lista os dentistas cadastrados
        model.addAttribute("consultaDTO", new ConsultaDTO());  // Formulário vazio para nova consulta
        return "consultasform";
    }

    @PostMapping("/salvar")
    public String salvarConsulta(@ModelAttribute ConsultaDTO consultaDTO) {
        // Verificar se o paciente e dentista foram selecionados
        Optional<Paciente> pacienteOpt = pacienteService.findById(consultaDTO.getPacienteId());
        Optional<Dentista> dentistaOpt = dentistaService.findById(consultaDTO.getDentistaId());

        if (!pacienteOpt.isPresent() || !dentistaOpt.isPresent()) {
            return "redirect:/consultasView/nova?erro";  // Redireciona com erro se paciente ou dentista não foram selecionados
        }

        // Criar e salvar a nova consulta
        Consulta consulta = new Consulta();
        consulta.setDataConsulta(consultaDTO.getDataConsulta());
        consulta.setStatus(consultaDTO.getStatus());
        consulta.setPaciente(pacienteOpt.get());
        consulta.setDentista(dentistaOpt.get());

        consultaService.save(consulta);

        return "redirect:/consultasView";  // Redireciona de volta para a lista de consultas
    }

    @GetMapping("/editar/{id}")
    public String editarConsulta(@PathVariable Long id, Model model) {
        Optional<Consulta> consultaOpt = consultaService.findById(id);
        if (!consultaOpt.isPresent()) {
            return "redirect:/consultasView";
        }

        Consulta consulta = consultaOpt.get();
        ConsultaDTO consultaDTO = new ConsultaDTO();
        consultaDTO.setIdConsulta(consulta.getIdConsulta());
        consultaDTO.setDataConsulta(consulta.getDataConsulta());
        consultaDTO.setStatus(consulta.getStatus());

        if (consulta.getDentista() != null) {
            consultaDTO.setDentistaId(consulta.getDentista().getIdDentista());
        }
        if (consulta.getPaciente() != null) {
            consultaDTO.setPacienteId(consulta.getPaciente().getId());
        }

        model.addAttribute("consultaDTO", consultaDTO);
        model.addAttribute("dentistas", dentistaService.findAll());
        model.addAttribute("pacientes", pacienteService.findAll());

        return "consultasedit";
    }

    @PostMapping("/editar/{id}")
    public String atualizarConsulta(@PathVariable Long id, @ModelAttribute ConsultaDTO consultaDTO) {
        Optional<Consulta> consultaOpt = consultaService.findById(id);
        if (!consultaOpt.isPresent()) {
            return "redirect:/consultasView";
        }

        Consulta consulta = consultaOpt.get();
        consulta.setDataConsulta(consultaDTO.getDataConsulta());
        consulta.setStatus(consultaDTO.getStatus());

        Optional<Paciente> pacienteOpt = pacienteService.findById(consultaDTO.getPacienteId());
        Optional<Dentista> dentistaOpt = dentistaService.findById(consultaDTO.getDentistaId());

        if (pacienteOpt.isPresent()) {
            consulta.setPaciente(pacienteOpt.get());
        }
        if (dentistaOpt.isPresent()) {
            consulta.setDentista(dentistaOpt.get());
        }

        consultaService.save(consulta);

        return "redirect:/consultasView";
    }
}
