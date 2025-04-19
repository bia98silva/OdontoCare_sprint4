package com.Fiap.OdontoCare.service;

import com.Fiap.OdontoCare.dto.NotificacaoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoService {

    @Autowired
    private JmsTemplate jmsTemplate;

    private static final String FILA_NOTIFICACOES = "fila.notificacoes";

    public void enviarNotificacaoConsulta(String destinatario, String assunto, String mensagem) {
        NotificacaoDTO notificacao = new NotificacaoDTO();
        notificacao.setDestinatario(destinatario);
        notificacao.setAssunto(assunto);
        notificacao.setMensagem(mensagem);

        jmsTemplate.convertAndSend(FILA_NOTIFICACOES, notificacao);
    }
}