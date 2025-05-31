package com.jrpolesi.exercises.exercise2;

import com.jrpolesi.http.HttpClient;

import java.io.IOException;

public class Exercise2 {
    private static final String URL = "https://apichallenges.eviltester.com/sim/entities/";

    public static void execute() {
        try {
            for (int i = 1; i < 9; i++) {
                getAndPrintEntity(i);
            }
        } catch (Exception e) {
            System.err.println("Erro ao executar exercise 2: " + e.getMessage());
            return;
        }
    }

    private static void getAndPrintEntity(int id) throws IOException {
        final var response = HttpClient.get(URL + id, Entity.class);

        final var statusCode = response.getStatusCode();
        System.out.println("Status code: " + statusCode);

        if (statusCode != 200) {
            System.err.println("Erro ao obter dados da API: " + statusCode);
            return;
        }

        final var entity = response.getBody();

        System.out.println("-> ID: " + entity.id() + ", Name: " + entity.name() + ", Description: " + entity.description());
    }
}
