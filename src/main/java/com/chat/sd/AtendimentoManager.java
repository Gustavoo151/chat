package com.chat.sd;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AtendimentoManager {
    private final BlockingQueue<Atendimento> fila = new LinkedBlockingQueue<>();
    private final BancoDeDados banco;
    private Atendimento atendimentoAtual;

    public AtendimentoManager(BancoDeDados banco) {
        this.banco = banco;
    }

    public void adicionarAtendimento(Atendimento atendimento) {
        fila.add(atendimento);
    }

    public void processarFila() {
        try {
            while (true) {
                atendimentoAtual = fila.take();
                atendimentoAtual.iniciar();
                atendimentoAtual.aguardarTermino();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void enviarMensagemAtual(String mensagem) {
        if (atendimentoAtual != null) {
            atendimentoAtual.enviarMensagem(mensagem);
        }
    }
}