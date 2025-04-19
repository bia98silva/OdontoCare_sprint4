package com.Fiap.OdontoCare.service;

import com.Fiap.OdontoCare.dto.NotificacaoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class NotificacaoConsumer {

    private static final Logger logger = LoggerFactory.getLogger(NotificacaoConsumer.class);

    @JmsListener(destination = "fila.notificacoes")
    public void receberNotificacao(NotificacaoDTO notificacao) {
        logger.info("Processando notificação: {}", notificacao);

        // Aqui você implementaria o envio real da notificação (email, SMS, etc.)
        logger.info("Notificação enviada para {} - Assunto: {}", notificacao.getDestinatario(), notificacao.getAssunto());
        logger.info("Mensagem: {}", notificacao.getMensagem());
    }
}