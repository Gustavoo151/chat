package com.chat.sd;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Atendimento {
    private final Socket clienteSocket;
    private final AtendimentoManager manager;
    private BufferedReader leitorCliente;
    private final BancoDeDados bancoDeDados;

    public Atendimento(Socket clienteSOcket, AtendimentoManager manager){
        this.clienteSocket = clienteSOcket;
        this.manager = manager;
        this.bancoDeDados = new BancoDeDados();
        this.leitorCliente = new BufferedReader(new InputStreamReader(System.in));
    }

}
