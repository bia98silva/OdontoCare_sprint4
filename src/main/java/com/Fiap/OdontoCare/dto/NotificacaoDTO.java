package com.Fiap.OdontoCare.dto;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class NotificacaoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String destinatario;
    private String assunto;
    private String mensagem;
    private LocalDateTime dataEnvio = LocalDateTime.now();
}