package com.jrpolesi.exercises.exercise11;

import com.jrpolesi.http.HttpClient;

public class Exercise11 {
    private static final String URL = "https://apichallenges.eviltester.com/sim/entities";

    public static void execute() {
        try {
            final var optionsResponse = HttpClient.options(URL);

            final var statusCode = optionsResponse.statusCode();
            System.out.println("Status code: " + statusCode);

            final var allowedMethods = optionsResponse.allowedMethods();
            System.out.println("Métodos permitidos: " + allowedMethods);
        } catch (Exception e) {
            System.err.println("Erro ao executar exercise 11: " + e.getMessage());
        }
    }
}
