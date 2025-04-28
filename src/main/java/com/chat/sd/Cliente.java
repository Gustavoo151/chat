package com.chat.sd;

import io.github.cdimascio.dotenv.Dotenv;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {
    static Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    private static final String SERVIDOR_ENDERECO = dotenv.get("SERVIDOR_ENDERECO");
    private static final int SERVIDOR_PORTA = Integer.parseInt(dotenv.get("PORTA"));


    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVIDOR_ENDERECO, SERVIDOR_PORTA);
             BufferedReader entradaServidor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter saidaServidor = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader entradaUsuario = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Conectado ao servidor.");

            Thread threadRecebimento = new Thread(() -> {
                try {
                    String mensagemServidor;
                    while ((mensagemServidor = entradaServidor.readLine()) != null) {
                        System.out.println(mensagemServidor);
                    }
                } catch (IOException e) {
                    System.err.println("Conexão com o servidor perdida: " + e.getMessage());
                }
            });
            threadRecebimento.start();

            String mensagemUsuario;
            while ((mensagemUsuario = entradaUsuario.readLine()) != null) {
                saidaServidor.println(mensagemUsuario);

                if ("SAIR".equalsIgnoreCase(mensagemUsuario.trim())) {
                    System.out.println("Encerrando conexão com o servidor...");
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("Erro na conexão com o servidor: " + e.getMessage());
        }
    }
}
