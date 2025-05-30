package com.jrpolesi.exercises.exercise4;

import com.google.gson.Gson;
import com.jrpolesi.http.HttpClient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

public class Exercise4 {
    private static final String URL = "https://apichallenges.eviltester.com/sim/entities";
    private static final Gson gson = new Gson();

    public static void execute() {
        try {
            final var queryParams = Map.of(
                    "categoria", "teste",
                    "limite", "5"
            );
            getAndPrintEntity(queryParams);
        } catch (Exception e) {
            System.err.println("Erro ao executar exercise 4: " + e.getMessage());
            return;
        }
    }

    private static void getAndPrintEntity(Map<String, String> queryParams) throws IOException {
        final var response = HttpClient.get(URL, queryParams);

        final var statusCode = response.getStatusCode();
        System.out.println("Status code: " + statusCode);

        if (statusCode == HttpURLConnection.HTTP_NOT_FOUND) {
            System.out.println("Entities não foram encontradas.");
            return;
        }

        if (statusCode != HttpURLConnection.HTTP_OK) {
            System.err.println("Erro ao obter dados da API: " + statusCode);
            return;
        }

        final var body = response.getBody();

        final var entitiesResponse = gson.fromJson(body, EntitiesResponse.class);

        System.out.println("Entities:");

        for (final var entity : entitiesResponse.entities) {
            System.out.println("-> ID: " + entity.id + ", Name: " + entity.name + ", Description: " + entity.description);
        }
    }
}
