package com.chat.sd;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Atendimento {
    private Socket socket;
    private PrintWriter saida;
    private BufferedReader entrada;
    private String email;
    private BancoDeDados banco;
    private Thread threadRecebimento;

    public Atendimento(Socket socket, BancoDeDados banco) {
        this.socket = socket;
        this.banco = banco;
    }

    public void iniciar() {
        try {
            saida = new PrintWriter(socket.getOutputStream(), true);
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            email = entrada.readLine();
            banco.iniciarAtendimento(email);

            threadRecebimento = new Thread(() -> {
                try {
                    String mensagem;
                    while ((mensagem = entrada.readLine()) != null) {
                        logMensagem(mensagem, false);
                    }
                } catch (IOException e) {
                    System.err.println("Erro na conexão: " + e.getMessage());
                } finally {
                    encerrar();
                }
            });
            threadRecebimento.start();

        } catch (IOException e) {
            System.err.println("Erro no atendimento: " + e.getMessage());
            encerrar();
        }
    }

    public void enviarMensagem(String mensagem) {
        saida.println(mensagem);
        logMensagem(mensagem, true);
    }

    private void logMensagem(String mensagem, boolean isServidor) {
        String tipo = isServidor ? "[Servidor] " : "[Cliente] ";
        String log = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) 
                     + " " + tipo + mensagem;
        
        String arquivo = "logs_atendimento/" 
                        + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) 
                        + "_" + email.replace("@", "at").replace(".", "_") + ".log";
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(arquivo, true))) {
            writer.println(log);
        } catch (IOException e) {
            System.err.println("Erro ao registrar mensagem: " + e.getMessage());
        }
    }

    public void encerrar() {
        try {
            if (saida != null) saida.close();
            if (entrada != null) entrada.close();
            if (socket != null) socket.close();
            banco.finalizarAtendimento(email);
        } catch (IOException e) {
            System.err.println("Erro ao encerrar conexão: " + e.getMessage());
        }
    }

    public void aguardarTermino() throws InterruptedException {
        threadRecebimento.join();
    }
}