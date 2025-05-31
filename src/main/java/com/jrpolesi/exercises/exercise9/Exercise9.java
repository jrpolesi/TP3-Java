package com.jrpolesi.exercises.exercise9;

import com.jrpolesi.http.HttpClient;

public class Exercise9 {
    private static final String URL = "https://apichallenges.eviltester.com/sim/entities/";

    public static void execute() {
        final var entityID = 9;

        try {
            final var deleteResponse = HttpClient.delete(URL + entityID);

            final var statusCode = deleteResponse.getStatusCode();
            System.out.println("Status code: " + statusCode);

            System.out.println("Buscando entity removida...");

            final var getResponse = HttpClient.get(URL + entityID, Entity.class);

            final var getStatusCode = getResponse.getStatusCode();
            System.out.println("Status code: " + getStatusCode);

            if (getStatusCode == 404) {
                System.out.println("Erro: " + getResponse.getErrorMessage());
            }
        } catch (Exception e) {
            System.err.println("Erro ao executar exercise 9: " + e.getMessage());
        }
    }
}
