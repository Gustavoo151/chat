package com.chat.sd;

import java.io.*;
import java.net.Socket;

public class Atendimento implements Runnable {
    private final Socket clienteSocket;
    private final AtendimentoManager manager;
    private BufferedReader leitorCliente;
    private PrintWriter escritorCliente;
    private BufferedReader leitorConsole;
    private String emailCliente;
    private final BancoDeDados bancoDeDados;

    public Atendimento(Socket clienteSocket, AtendimentoManager manager) {
        this.clienteSocket = clienteSocket;
        this.manager = manager;
        this.bancoDeDados = new BancoDeDados();
        this.leitorConsole = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {
        try {
            inicializarStreams();
            solicitarEmail();
            iniciarAtendimento();
        } catch (IOException e) {
            System.err.println("Erro durante o atendimento: " + e.getMessage());
        } finally {
            encerrarAtendimento();
        }
    }

    private void inicializarStreams() throws IOException {
        leitorCliente = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
        escritorCliente = new PrintWriter(clienteSocket.getOutputStream(), true);
    }

    private void solicitarEmail() throws IOException {
        escritorCliente.println("Bem-vindo ao sistema de atendimento. Por favor, informe seu e-mail:");
        emailCliente = leitorCliente.readLine();

        // Registro do início do atendimento no banco de dados
        bancoDeDados.iniciarAtendimento(emailCliente);
    }

    private void iniciarAtendimento() {
        // Thread para ler mensagens do cliente
        Thread threadLeituraCliente = new Thread(() -> {
            try {
                String mensagem;
                while ((mensagem = leitorCliente.readLine()) != null) {
                    if ("SAIR".equalsIgnoreCase(mensagem.trim())) {
                        System.out.println("Cliente solicitou encerramento do atendimento.");
                        break;
                    }

                    System.out.println("Cliente [" + emailCliente + "]: " + mensagem);
                    bancoDeDados.registrarMensagem(emailCliente, "CLIENTE", mensagem);
                }
            } catch (IOException e) {
                System.err.println("Erro ao ler mensagem do cliente: " + e.getMessage());
            }
        });

        Thread threadEnvioServidor = new Thread(() -> {
            try {
                escritorCliente.println("Atendimento iniciado. Digite suas mensagens (ou SAIR para encerrar):");

                String mensagem;
                while (true) {
                    System.out.println("Digite sua resposta para o cliente (ou ENCERRAR para finalizar):");
                    mensagem = leitorConsole.readLine();

                    if (mensagem == null || "ENCERRAR".equalsIgnoreCase(mensagem.trim())) {
                        break;
                    }

                    escritorCliente.println("Atendente: " + mensagem);
                    bancoDeDados.registrarMensagem(emailCliente, "ATENDENTE", mensagem);
                }

                escritorCliente.println("Atendimento encerrado pelo atendente. Obrigado!");
            } catch (IOException e) {
                System.err.println("Erro ao enviar mensagem para o cliente: " + e.getMessage());
            }
        });

        threadLeituraCliente.start();
        threadEnvioServidor.start();

        try {
            threadLeituraCliente.join();
            threadEnvioServidor.join();
        } catch (InterruptedException e) {
            System.err.println("Atendimento interrompido: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private void encerrarAtendimento() {
        try {
            System.out.println("Encerrando atendimento para cliente: " + emailCliente);

            if (leitorCliente != null) leitorCliente.close();
            if (escritorCliente != null) escritorCliente.close();
            if (clienteSocket != null && !clienteSocket.isClosed()) clienteSocket.close();

            bancoDeDados.finalizarAtendimento(emailCliente);

            manager.finalizarAtendimento();
        } catch (IOException e) {
            System.err.println("Erro ao encerrar o atendimento: " + e.getMessage());
        }
    }
}