package com.chat.sd;

import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ServidorMain {
    private static final int PORTA = 8080;
    private final Queue<Socket> filaClientes = new ConcurrentLinkedDeque<>();
    private final AtendimentoManager atendimentoManager;

    public ServidorMain() {
        this.atendimentoManager = new AtendimentoManager(filaClientes);
    }
}
