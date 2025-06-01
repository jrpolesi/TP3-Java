package com.jrpolesi.exercises.exercise10;

import com.jrpolesi.http.HttpClient;

public class Exercise10 {
    private static final String URL = "https://apichallenges.eviltester.com/sim/entities/";

    public static void execute() {
        final var entityID = 2;

        try {
            final var deleteResponse = HttpClient.delete(URL + entityID);

            final var statusCode = deleteResponse.getStatusCode();
            System.out.println("Status code: " + statusCode);

            if (statusCode != 200) {
                System.out.println("Erro: " + deleteResponse.getErrorMessage());
            }
        } catch (Exception e) {
            System.err.println("Erro ao executar exercise 10: " + e.getMessage());
        }
    }
}
