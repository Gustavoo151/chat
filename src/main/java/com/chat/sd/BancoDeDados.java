package com.chat.sd;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;

public class BancoDeDados {
    private static final String DIRETORIO_LOGS = "logs_atendimento";
    private  static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public BancoDeDados() {
        try {
            Path diretorio = Paths.get(DIRETORIO_LOGS);
            if(!diretorio.toFile().exists()){
                Files.createDirectories(diretorio);
                System.out.println("Diretório de logs criado: " + diretorio.toAbsolutePath());
            }

        }catch (Exception e){
            System.err.println("Erro ao criar diretório de logs: " + e.getMessage());
        };
    }
}
