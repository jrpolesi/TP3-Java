package com.jrpolesi.exercises.exercise8;

import com.jrpolesi.http.HttpClient;

public class Exercise8 {
    private static final String URL = "https://apichallenges.eviltester.com/sim/entities/";

    public static void execute() {
        final var entityID = 10;
        final var myEntity = Entity.of("atualizado");

        try {
            final var postResponse = HttpClient.put(URL + entityID, myEntity, Entity.class);

            final var statusCode = postResponse.getStatusCode();
            System.out.println("Status code: " + statusCode);

            final var entity = postResponse.getBody();
            System.out.println("Entity atualizada:");
            System.out.println("-> ID: " + entity.id + ", Name: " + entity.name + ", Description: " + entity.description);

            System.out.println("Buscando entity atualizada...");

            final var getResponse = HttpClient.get(URL + entity.id, Entity.class);

            final var getStatusCode = getResponse.getStatusCode();
            System.out.println("Status code: " + getStatusCode);

            final var fetchedEntity = getResponse.getBody();
            System.out.println("Entity buscada:");
            System.out.println("-> ID: " + fetchedEntity.id + ", Name: " + fetchedEntity.name + ", Description: " + fetchedEntity.description);
        } catch (Exception e) {
            System.err.println("Erro ao executar exercise 8: " + e.getMessage());
        }
    }
}
