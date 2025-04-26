package com.chat.sd;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServidorMain {
    public static void main(String[] args) {
        BancoDeDados banco = new BancoDeDados();
        AtendimentoManager manager = new AtendimentoManager(banco);

        new Thread(manager::processarFila).start();

        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Servidor aguardando conexões...");

            // Thread para aceitar conexões
            new Thread(() -> {
                while (true) {
                    try {
                        Socket socket = serverSocket.accept();
                        Atendimento atendimento = new Atendimento(socket, banco);
                        manager.adicionarAtendimento(atendimento);
                    } catch (IOException e) {
                        System.err.println("Erro ao aceitar conexão: " + e.getMessage());
                    }
                }
            }).start();

            // Loop para enviar mensagens para o cliente atual
            try (Scanner scanner = new Scanner(System.in)) {
                while (true) {
                    System.out.print("Digite uma mensagem para o cliente atual: ");
                    String mensagem = scanner.nextLine();
                    manager.enviarMensagemAtual(mensagem);
                }
            }

        } catch (IOException e) {
            System.err.println("Erro no servidor: " + e.getMessage());
        }
    }
}