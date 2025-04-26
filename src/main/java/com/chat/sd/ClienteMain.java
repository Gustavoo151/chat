package com.chat.sd;

import java.io.*;
import java.net.Socket;

public class ClienteMain {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))) {
            
            System.out.print("Informe seu email: ");
            String email = teclado.readLine();
            saida.println(email);

            Thread recebedora = new Thread(() -> {
                try {
                    String mensagem;
                    while ((mensagem = entrada.readLine()) != null) {
                        System.out.println("Servidor: " + mensagem);
                    }
                } catch (IOException e) {
                    System.out.println("Conexão encerrada.");
                }
            });
            recebedora.start();

            String mensagemUsuario;
            while ((mensagemUsuario = teclado.readLine()) != null) {
                saida.println(mensagemUsuario);
            }

        } catch (IOException e) {
            System.err.println("Erro no cliente: " + e.getMessage());
        }
    }
}