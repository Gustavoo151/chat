package com.chat.sd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {
//    private static final String SERVIDOR_ENDERECO = "localhost";
//    private static final int SERVIDOR_PORTA = 8080;
//
//    public static void main(String[] args) {
//        try (Socket socket = new Socket(SERVIDOR_ENDERECO, SERVIDOR_PORTA);
//             BufferedReader entradaServidor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//             PrintWriter saidaServidor = new PrintWriter(socket.getOutputStream(), true);
//             BufferedReader entradaUsuario = new BufferedReader(new InputStreamReader(System.in))) {
//
//            System.out.println("Conectado ao servidor.");
//
//            // Thread para receber mensagens do servidor
//            Thread threadRecebimento = new Thread(() -> {
//                try {
//                    String mensagemServidor;
//                    while ((mensagemServidor = entradaServidor.readLine()) != null) {
//                        System.out.println(mensagemServidor);
//                    }
//                } catch (IOException e) {
//                    System.err.println("Conexão com o servidor perdida: " + e.getMessage());
//                }
//            });
//            threadRecebimento.start();
//
//            // Envio de mensagens para o servidor
//            String mensagemUsuario;
//            while ((mensagemUsuario = entradaUsuario.readLine()) != null) {
//                saidaServidor.println(mensagemUsuario);
//
//                if ("SAIR".equalsIgnoreCase(mensagemUsuario.trim())) {
//                    System.out.println("Encerrando conexão com o servidor...");
//                    break;
//                }
//            }
//
//        } catch (IOException e) {
//            System.err.println("Erro na conexão com o servidor: " + e.getMessage());
//        }
//    }
}
