package com.chat.sd;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import io.github.cdimascio.dotenv.Dotenv;


public class ServidorMain {
    static Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    private static final int PORTA = Integer.parseInt(dotenv.get("PORTA"));
    private final Queue<Socket> filaClientes = new ConcurrentLinkedQueue<>();
    private final AtendimentoManager atendimentoManager;

    public ServidorMain() {
        this.atendimentoManager = new AtendimentoManager(filaClientes);
    }

    public void iniciar() {
        Thread threadManager = new Thread(atendimentoManager);
        threadManager.start();

        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
            System.out.println("Servidor iniciado na porta " + PORTA);
            System.out.println("Aguardando conexões...");

            while (true) {
                Socket socketCliente = serverSocket.accept();
                System.out.println("Nova conexão: " + socketCliente.getInetAddress().getHostAddress());

                filaClientes.add(socketCliente);
                System.out.println("Cliente adicionado à fila. Tamanho atual: " + filaClientes.size());
            }
        } catch (IOException e) {
            System.err.println("Erro ao iniciar o servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ServidorMain servidor = new ServidorMain();
        servidor.iniciar();
    }
}