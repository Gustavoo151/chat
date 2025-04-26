package com.chat.sd;

import java.net.Socket;
import java.util.Queue;

public class AtendimentoManager {
    private final Queue<Socket> filaClientes;
    private boolean atendimentoEmAndamento = false;


    public AtendimentoManager(Queue<Socket> filaClientes) {
        this.filaClientes = filaClientes;
    }


    private void iniciarAtendimento(Socket clienteSocket) {
        atendimentoEmAndamento = true;
        System.out.println("Iniciando atendimento para cliente: " +
                clienteSocket.getInetAddress().getHostAddress());

        Atendimento atendimento = new Atendimento(clienteSocket, this);
        Thread threadAtendimento = new Thread(atendimento);
        threadAtendimento.start();
    }
}
