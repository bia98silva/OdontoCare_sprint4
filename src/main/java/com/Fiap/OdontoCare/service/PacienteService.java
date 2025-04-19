package com.Fiap.OdontoCare.service;

import com.Fiap.OdontoCare.entity.Consulta;
import com.Fiap.OdontoCare.entity.Paciente;
import com.Fiap.OdontoCare.repository.ConsultaRepository;
import com.Fiap.OdontoCare.repository.PacienteRepository;
import com.Fiap.OdontoCare.dto.PacienteDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    public long count() {
        return pacienteRepository.count();
    }

    public List<Paciente> findAll() {
        return pacienteRepository.findAll();
    }

    public Optional<Paciente> findById(Long id) {
        return pacienteRepository.findById(id);
    }

    public Paciente save(PacienteDTO pacienteDTO) {
        Paciente paciente = new Paciente();
        paciente.setId(pacienteDTO.getId());
        paciente.setNome(pacienteDTO.getNome());
        paciente.setCpf(pacienteDTO.getCpf());
        paciente.setCarteirinha(pacienteDTO.getCarteirinha());
        paciente.setTelefone(pacienteDTO.getTelefone());
        paciente.setEmail(pacienteDTO.getEmail());
        paciente.setEndereco(pacienteDTO.getEndereco());

        paciente.setDataNascimento(pacienteDTO.getDataNascimento());

        return pacienteRepository.save(paciente);
    }

    @Transactional
    public void criarPacienteEConsulta(Paciente paciente, Consulta consulta) {
        pacienteRepository.save(paciente);
        consulta.setPaciente(paciente);
        consultaRepository.save(consulta);
    }


    public Paciente update(PacienteDTO pacienteDTO) {
        Paciente paciente = new Paciente();
        paciente.setId(pacienteDTO.getId());
        paciente.setNome(pacienteDTO.getNome());
        paciente.setCpf(pacienteDTO.getCpf());
        paciente.setCarteirinha(pacienteDTO.getCarteirinha());
        paciente.setTelefone(pacienteDTO.getTelefone());
        paciente.setEmail(pacienteDTO.getEmail());
        paciente.setEndereco(pacienteDTO.getEndereco());

        paciente.setDataNascimento(pacienteDTO.getDataNascimento());

        return pacienteRepository.save(paciente);
    }

    public void deleteById(Long id) {
        pacienteRepository.deleteById(id);
    }
}
