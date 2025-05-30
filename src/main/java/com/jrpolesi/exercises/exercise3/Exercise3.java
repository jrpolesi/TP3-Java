package com.jrpolesi.exercises.exercise3;

import java.io.IOException;
import java.net.HttpURLConnection;

import com.google.gson.Gson;
import com.jrpolesi.http.HttpClient;

public class Exercise3 {
    private static final String URL = "https://apichallenges.eviltester.com/sim/entities/";
    private static final Gson gson = new Gson();

    public static void execute() {
        try {
            getAndPrintEntity(13);
        } catch (Exception e) {
            System.err.println("Erro ao executar exercise 3: " + e.getMessage());
            return;
        }
    }

    private static void getAndPrintEntity(int id) throws IOException {
        final var response = HttpClient.get(URL + id);

        final var statusCode = response.getStatusCode();
        System.out.println("Status code: " + statusCode);

        if (statusCode == HttpURLConnection.HTTP_NOT_FOUND) {
            System.out.println("Entidade com o ID " + id + " não foi encontrada.");
            return;
        }

        if (statusCode != HttpURLConnection.HTTP_OK) {
            System.err.println("Erro ao obter dados da API: " + statusCode);
            return;
        }

        final var body = response.getBody();

        final var entity = gson.fromJson(body, Entity.class);

        System.out.println("-> ID: " + entity.id() + ", Name: " + entity.name() + ", Description: " + entity.description());
    }
}
