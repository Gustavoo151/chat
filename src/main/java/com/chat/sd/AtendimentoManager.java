package com.chat.sd;

import java.net.Socket;
import java.util.Queue;

public class AtendimentoManager {
    private final Queue<Socket> filaClientes;

    public AtendimentoManager(Queue<Socket> filaClientes) {
        this.filaClientes = filaClientes;
    }
}
