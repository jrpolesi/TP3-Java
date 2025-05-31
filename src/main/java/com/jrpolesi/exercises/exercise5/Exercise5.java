package com.jrpolesi.exercises.exercise5;

import com.jrpolesi.http.HttpClient;

public class Exercise5 {
    private static final String URL = "https://apichallenges.eviltester.com/sim/entities";

    public static void execute() {
        final var myEntity = Entity.of("aluno");

        try {
            final var response = HttpClient.post(URL, myEntity, Entity.class);

            final var statusCode = response.getStatusCode();
            System.out.println("Status code: " + statusCode);

            final var entity = response.getBody();
            System.out.println("Entity criada:");
            System.out.println("-> ID: " + entity.id + ", Name: " + entity.name + ", Description: " + entity.description);

        } catch (Exception e) {
            System.err.println("Erro ao executar exercise 5: " + e.getMessage());
        }
    }
}
