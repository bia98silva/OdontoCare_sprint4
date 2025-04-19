package com.Fiap.OdontoCare.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConsultaDTO {

    private Long idConsulta;

    @NotNull(message = "A data da consulta é obrigatória")
    private LocalDateTime dataConsulta;

    @NotNull(message = "O status da consulta é obrigatório")
    private String status;

    private String detalhes;

    @NotNull(message = "O ID do paciente é obrigatório")
    private Long pacienteId;

    @NotNull(message = "O ID do dentista é obrigatório")
    private Long dentistaId;

    private PacienteDTO pacienteDTO;

    public void setIdConsulta(Long idConsulta) {
        this.idConsulta = idConsulta;
    }
}
