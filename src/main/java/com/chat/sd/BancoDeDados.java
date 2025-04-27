package com.chat.sd;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import io.github.cdimascio.dotenv.Dotenv;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class BancoDeDados {
    Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> chatCollection;



    public BancoDeDados() {
        try {
            String connectionString = System.getenv(dotenv.get("MONGODB_URI"));
            if (connectionString == null || connectionString.isEmpty()) {
                connectionString = dotenv.get("CONNECTION_STRING");
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