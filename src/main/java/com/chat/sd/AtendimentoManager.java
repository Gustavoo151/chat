package com.chat.sd;

import java.net.Socket;
import java.util.Queue;

public class AtendimentoManager implements Runnable {
    private final Queue<Socket> filaClientes;
    private boolean atendimentoEmAndamento = false;

    public AtendimentoManager(Queue<Socket> filaClientes) {
        this.filaClientes = filaClientes;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (!filaClientes.isEmpty() && !atendimentoEmAndamento) {
                    Socket clienteAtual = filaClientes.poll();
                    iniciarAtendimento(clienteAtual);
                }

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println("Gerenciador de atendimento interrompido: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    private void iniciarAtendimento(Socket clienteSocket) {
        atendimentoEmAndamento = true;
        System.out.println("Iniciando atendimento para cliente: " +
                clienteSocket.getInetAddress().getHostAddress());

        Atendimento atendimento = new Atendimento(clienteSocket, this);
        Thread threadAtendimento = new Thread(atendimento);
        threadAtendimento.start();
    }

    public void finalizarAtendimento() {
        atendimentoEmAndamento = false;
        System.out.println("Atendimento finalizado. Verificando próximo cliente na fila...");
    }
}
