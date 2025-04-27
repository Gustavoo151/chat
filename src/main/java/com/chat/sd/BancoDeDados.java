package com.chat.sd;

import javax.swing.text.Document;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BancoDeDados {
    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> chatCollection;


    public BancoDeDados() {
        try {
            // Configure a conexão MongoDB
            // Use environment variable for connection string or default to localhost
            String connectionString = System.getenv("MONGODB_URI");
            if (connectionString == null || connectionString.isEmpty()) {
                connectionString = "mongodb://localhost:27017";
            }

            mongoClient = MongoClients.create(connectionString);
            database = mongoClient.getDatabase("chat_sd");
            chatCollection = database.getCollection("atendimentos");

            System.out.println("Conexão com MongoDB estabelecida");
        } catch (Exception e) {
            System.err.println("Erro ao conectar com MongoDB: " + e.getMessage());
        }
    }

    public void iniciarAtendimento(String email) {
        try {
            String dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            Document atendimento = new Document()
                    .append("email", email)
                    .append("dataInicio", dataHora)
                    .append("mensagens", new ArrayList<Document>())
                    .append("status", "ativo");

            chatCollection.insertOne(atendimento);
            System.out.println("Atendimento iniciado e registrado para: " + email);
        } catch (Exception e) {
            System.err.println("Erro ao registrar início do atendimento: " + e.getMessage());
        }
    }

    public void registrarMensagem(String email, String origem, String mensagem) {
        try {
            String dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

            Document novaMensagem = new Document()
                    .append("timestamp", dataHora)
                    .append("origem", origem)
                    .append("conteudo", mensagem);

            Bson filter = Filters.and(
                    Filters.eq("email", email),
                    Filters.eq("status", "ativo")
            );

            Bson update = Updates.push("mensagens", novaMensagem);
            chatCollection.updateOne(filter, update);

            System.out.println("Mensagem registrada para: " + email);
        } catch (Exception e) {
            System.err.println("Erro ao registrar mensagem: " + e.getMessage());
        }
    }

    public void finalizarAtendimento(String email) {
        try {
            String dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            Bson filter = Filters.and(
                    Filters.eq("email", email),
                    Filters.eq("status", "ativo")
            );

            Bson update = Updates.combine(
                    Updates.set("dataFim", dataHora),
                    Updates.set("status", "finalizado")
            );

            chatCollection.updateOne(filter, update);
            System.out.println("Atendimento finalizado e registrado para: " + email);
        } catch (Exception e) {
            System.err.println("Erro ao registrar fim do atendimento: " + e.getMessage());
        }
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Conexão MongoDB fechada");
        }
    }
}