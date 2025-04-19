package com.Fiap.OdontoCare.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Consulta")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Consulta")
    private Long idConsulta;

    @Column(name = "Data_Consulta")
    private LocalDateTime dataConsulta;

    @Column(name = "Status", length = 50)
    private String status;

    @Column(name = "Detalhes", length = 255)
    private String detalhes;

    private String dataConsultaFormatada;

    @ManyToOne
    @JoinColumn(name = "ID_Paciente")
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "ID_Dentista")
    private Dentista dentista;

    public Long getPacienteId() {
        return paciente != null ? paciente.getId() : null;
    }

    public void setPacienteId(Long pacienteId) {
        if (paciente != null) {
            paciente.setId(pacienteId);
        }
    }

    public Long getDentistaId() {
        return dentista != null ? dentista.getIdDentista() : null;
    }

    public void setDentistaId(Long dentistaId) {
        if (dentista != null) {
            dentista.setIdDentista(dentistaId);
        }
    }

    public String getDataConsultaFormatada() {
        return dataConsultaFormatada;
    }

    public void setDataConsultaFormatada(String dataConsultaFormatada) {
        this.dataConsultaFormatada = dataConsultaFormatada;
    }
}
