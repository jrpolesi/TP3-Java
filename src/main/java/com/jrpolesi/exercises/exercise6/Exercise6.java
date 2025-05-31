package com.jrpolesi.exercises.exercise6;

import com.jrpolesi.http.HttpClient;

public class Exercise6 {
    private static final String URL = "https://apichallenges.eviltester.com/sim/entities";

    public static void execute() {
        final var myEntity = Entity.of("aluno");

        try {
            final var postResponse = HttpClient.post(URL, myEntity, Entity.class);

            final var statusCode = postResponse.getStatusCode();
            System.out.println("Status code: " + statusCode);

            final var entity = postResponse.getBody();
            System.out.println("Entity criada:");
            System.out.println("-> ID: " + entity.id + ", Name: " + entity.name + ", Description: " + entity.description);

            System.out.println("Buscando entity criada...");

            final var getResponse = HttpClient.get(URL + "/" + entity.id, Entity.class);

            final var getStatusCode = getResponse.getStatusCode();
            System.out.println("Status code: " + getStatusCode);

            final var fetchedEntity = getResponse.getBody();
            System.out.println("Entity buscada:");
            System.out.println("-> ID: " + fetchedEntity.id + ", Name: " + fetchedEntity.name + ", Description: " + fetchedEntity.description);

        } catch (Exception e) {
            System.err.println("Erro ao executar exercise 6: " + e.getMessage());
        }
    }
}
