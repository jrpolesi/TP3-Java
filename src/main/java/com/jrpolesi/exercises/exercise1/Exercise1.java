package com.jrpolesi.exercises.exercise1;

import com.google.gson.Gson;
import com.jrpolesi.http.HttpClient;

public class Exercise1 {
    public static void execute() {
        final var url = "https://apichallenges.eviltester.com/sim/entities";

        try {
            final var response = HttpClient.get(url);

            final var statusCode = response.getStatusCode();
            System.out.println("Status code: " + statusCode);

            if (statusCode != 200) {
                System.err.println("Erro ao obter dados da API: " + statusCode);
                return;
            }

            final var body = response.getBody();

            final var gson = new Gson();
            final var entitiesResponse = gson.fromJson(body, EntitiesResponse.class);

            System.out.println("Entities:");

            for (final var entity : entitiesResponse.entities) {
                System.out.println("-> ID: " + entity.id + ", Name: " + entity.name + ", Description: " + entity.description);
            }
        } catch (Exception e) {
            System.err.println("Erro ao executar exercise 1: " + e.getMessage());
            return;
        }
    }
}
