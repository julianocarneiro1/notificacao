package com.jmc.notificacao.listener;

import com.jmc.notificacao.constante.MensagemConstante;
import com.jmc.notificacao.domain.Proposta;
import com.jmc.notificacao.service.NotificacaoSnsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PropostaConcluidaListener {

    @Autowired
    private NotificacaoSnsService notificacaoSnsService;

    @RabbitListener(queues = "${rabbitmq.queue.proposta.concluida}")
    public void propostaConcluida(Proposta proposta) {
        String mensagem;

        if (proposta.getAprovada()) {
            mensagem = String.format(MensagemConstante.PROPOSTA_APROVADA, proposta.getUsuario().getNome());
        } else {
            mensagem = String.format(MensagemConstante.PROPOSTA_REPROVADA, proposta.getUsuario().getNome(), proposta.getObservacao());
        }

        notificacaoSnsService.notificar(proposta.getUsuario().getTelefone(), mensagem);
    }
}
