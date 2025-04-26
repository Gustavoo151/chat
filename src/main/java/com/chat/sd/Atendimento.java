package com.chat.sd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Atendimento implements Runnable{
    private final Socket clienteSocket;
    private final AtendimentoManager manager;
    private BufferedReader leitorCliente;
    private PrintWriter escritorCliente;
    private BufferedReader leitorConsole;
    private String emailCliente;
    private final BancoDeDados bancoDeDados;

    public Atendimento(Socket clienteSOcket, AtendimentoManager manager){
        this.clienteSocket = clienteSOcket;
        this.manager = manager;
        this.bancoDeDados = new BancoDeDados();
        this.leitorCliente = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run(){
        try {
            inicializarStreams();
            solicitarEmail();
            inicializarStreams();
        }catch (Exception e){
            System.err.println("Erro durante o atendimento: " + e.getMessage());

        }   finally {
            encerrarAtendimento();
        }
    }



    private void inicializarStreams() throws IOException {
        leitorCliente = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
        escritorCliente = new PrintWriter(clienteSocket.getOutputStream(), true);
    }

    public void solicitarEmail() throws IOException {
        escritorCliente.println("Bem-vindo ao sistema de atendimento. Por favor, informe seu e-mail: ");
        emailCliente = leitorCliente.readLine();

        bancoDeDados.iniciarAtendimento(emailCliente);
    }

    private void encerrarAtendimento() {
        try {
            System.out.println("Encerrando atendimento para o cliente: " + emailCliente);

            if(leitorCliente != null) leitorCliente.close();
            if(escritorCliente != null) escritorCliente.close();
            if(clienteSocket != null && !clienteSocket.isClosed()) clienteSocket.close();

            bancoDeDados.finalizarAtendimento(emailCliente);

            manager.finalizarAtendimento();
        } catch (IOException e) {
            System.err.println("Erro ao encerrar o atendimento: " + e.getMessage());
        }
    }
}
