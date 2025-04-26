package com.chat.sd;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BancoDeDados {
    private static final String DIRETORIO_LOGS = "logs_atendimento";
    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    public BancoDeDados() {
        try {
            Path diretorio = Paths.get(DIRETORIO_LOGS);
            if (!Files.exists(diretorio)) {
                Files.createDirectories(diretorio);
                System.out.println("Diretório de logs criado: " + diretorio.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Erro ao criar diretório de logs: " + e.getMessage());
        }
    }

    public void iniciarAtendimento(String email) {
        String nomeArquivo = gerarNomeArquivo(email);
        try (PrintWriter writer = new PrintWriter(new FileWriter(nomeArquivo, true))) {
            String dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.println("=== INÍCIO DO ATENDIMENTO - " + dataHora + " ===");
            writer.println("Cliente: " + email);
            writer.println();

            System.out.println("Atendimento iniciado e registrado para: " + email);
        } catch (IOException e) {
            System.err.println("Erro ao registrar início do atendimento: " + e.getMessage());
        }
    }

    public void registrarMensagem(String email, String origem, String mensagem) {
        String nomeArquivo = gerarNomeArquivo(email);
        try (PrintWriter writer = new PrintWriter(new FileWriter(nomeArquivo, true))) {
            String dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            writer.println("[" + dataHora + "] " + origem + ": " + mensagem);

            System.out.println("Mensagem registrada para: " + email);
        } catch (IOException e) {
            System.err.println("Erro ao registrar mensagem: " + e.getMessage());
        }
    }

    public void finalizarAtendimento(String email) {
        String nomeArquivo = gerarNomeArquivo(email);
        try (PrintWriter writer = new PrintWriter(new FileWriter(nomeArquivo, true))) {
            String dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.println();
            writer.println("=== FIM DO ATENDIMENTO - " + dataHora + " ===");

            System.out.println("Atendimento finalizado e registrado para: " + email);
        } catch (IOException e) {
            System.err.println("Erro ao registrar fim do atendimento: " + e.getMessage());
        }
    }

    private String gerarNomeArquivo(String email) {
        String dataHoje = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String emailFormatado = email.replace("@", "_at_").replace(".", "_");
        return DIRETORIO_LOGS + "/" + dataHoje + "_" + emailFormatado + ".log";
    }
}