package com.chat.sd;

import java.net.Socket;
import java.util.Queue;

public class AtendimentoManager {
    private final Queue<Socket> filaClientes;
    private boolean atendimentoEmAndamento = false;


    public AtendimentoManager(Queue<Socket> filaClientes) {
        this.filaClientes = filaClientes;
    }
}
