package com.Fiap.OdontoCare.service;

import com.Fiap.OdontoCare.dto.ConsultaDTO;
import com.Fiap.OdontoCare.entity.Consulta;
import com.Fiap.OdontoCare.entity.Dentista;
import com.Fiap.OdontoCare.entity.Paciente;
import com.Fiap.OdontoCare.repository.ConsultaRepository;
import com.Fiap.OdontoCare.repository.DentistaRepository;
import com.Fiap.OdontoCare.repository.PacienteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final DentistaRepository dentistaRepository;
    private final NotificacaoService notificacaoService;

    public ConsultaService(
            ConsultaRepository consultaRepository,
            PacienteRepository pacienteRepository,
            DentistaRepository dentistaRepository,
            NotificacaoService notificacaoService) {
        this.consultaRepository = consultaRepository;
        this.pacienteRepository = pacienteRepository;
        this.dentistaRepository = dentistaRepository;
        this.notificacaoService = notificacaoService;
    }

    public long count() {
        return consultaRepository.count();
    }

    public List<Consulta> listarProximasConsultas() {
        return consultaRepository.findByDataConsultaAfter(LocalDate.now());
    }

    public List<Consulta> findAll() {
        return consultaRepository.findAll();
    }

    public Optional<Consulta> findById(Long id) {
        return consultaRepository.findById(id);
    }

    public Consulta save(Consulta consulta) {
        Paciente paciente = pacienteRepository.findById(consulta.getPacienteId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado"));

        Dentista dentista = dentistaRepository.findById(consulta.getDentistaId())
                .orElseThrow(() -> new IllegalArgumentException("Dentista não encontrado"));

        Consulta consultaParaSalvar = new Consulta();
        if (consulta.getIdConsulta() != null) {
            consultaParaSalvar.setIdConsulta(consulta.getIdConsulta());
        }
        consultaParaSalvar.setDataConsulta(consulta.getDataConsulta());
        consultaParaSalvar.setStatus(consulta.getStatus());
        consultaParaSalvar.setDetalhes(consulta.getDetalhes());
        consultaParaSalvar.setPaciente(paciente);
        consultaParaSalvar.setDentista(dentista);

        Consulta consultaSalva = consultaRepository.save(consultaParaSalvar);

        if (paciente.getEmail() != null && !paciente.getEmail().isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String dataFormatada = consultaSalva.getDataConsulta().format(formatter);

            String assunto;
            String mensagem;

            if (consulta.getIdConsulta() == null) {
                // Nova consulta
                assunto = "OdontoCare - Nova Consulta Agendada";
                mensagem = String.format(
                        "Olá %s,\n\nSua consulta foi agendada com sucesso para %s com o(a) Dr(a). %s.\n" +
                                "Status: %s\n\n" +
                                "Por favor, chegue com 15 minutos de antecedência.\n\n" +
                                "Atenciosamente,\n" +
                                "Equipe OdontoCare",
                        paciente.getNome(),
                        dataFormatada,
                        dentista.getNome(),
                        consulta.getStatus()
                );
            } else {
                assunto = "OdontoCare - Atualização de Consulta";
                mensagem = String.format(
                        "Olá %s,\n\nSua consulta foi atualizada para %s com o(a) Dr(a). %s.\n" +
                                "Status: %s\n\n" +
                                "Por favor, chegue com 15 minutos de antecedência.\n\n" +
                                "Atenciosamente,\n" +
                                "Equipe OdontoCare",
                        paciente.getNome(),
                        dataFormatada,
                        dentista.getNome(),
                        consulta.getStatus()
                );
            }

            // Enviar a notificação
            notificacaoService.enviarNotificacaoConsulta(
                    paciente.getEmail(),
                    assunto,
                    mensagem
            );
        }

        return consultaSalva;
    }

    public Consulta update(ConsultaDTO consultaDTO) {
        Paciente paciente = pacienteRepository.findById(consultaDTO.getPacienteId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado"));

        Dentista dentista = dentistaRepository.findById(consultaDTO.getDentistaId())
                .orElseThrow(() -> new IllegalArgumentException("Dentista não encontrado"));

        Consulta consulta = consultaRepository.findById(consultaDTO.getIdConsulta())
                .orElseThrow(() -> new IllegalArgumentException("Consulta não encontrada"));

        consulta.setDataConsulta(consultaDTO.getDataConsulta());
        consulta.setStatus(consultaDTO.getStatus());
        consulta.setDetalhes(consultaDTO.getDetalhes());
        consulta.setPaciente(paciente);
        consulta.setDentista(dentista);
        return this.save(consulta);
    }

    public void deleteById(Long id) {
        Optional<Consulta> consultaOpt = consultaRepository.findById(id);
        if (consultaOpt.isPresent()) {
            Consulta consulta = consultaOpt.get();
            Paciente paciente = consulta.getPaciente();

            if (paciente != null && paciente.getEmail() != null && !paciente.getEmail().isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                String dataFormatada = consulta.getDataConsulta().format(formatter);

                String assunto = "OdontoCare - Consulta Cancelada";
                String mensagem = String.format(
                        "Olá %s,\n\nSua consulta agendada para %s foi cancelada.\n\n" +
                                "Se você não solicitou este cancelamento, por favor entre em contato conosco.\n\n" +
                                "Atenciosamente,\n" +
                                "Equipe OdontoCare",
                        paciente.getNome(),
                        dataFormatada
                );

                notificacaoService.enviarNotificacaoConsulta(
                        paciente.getEmail(),
                        assunto,
                        mensagem
                );
            }
        }

        consultaRepository.deleteById(id);
    }

    public void update(Consulta consulta) {
        // Usar o método save que já inclui notificação
        this.save(consulta);
    }
}