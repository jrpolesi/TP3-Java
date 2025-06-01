package com.jrpolesi.exercises.exercise12;

import com.jrpolesi.http.APIResponse;
import com.jrpolesi.http.HttpClient;

import java.io.IOException;
import java.net.HttpURLConnection;

public class Exercise12 {
    private static final String URL = "https://apichallenges.eviltester.com/simpleapi";
    private static final String ITEMS_PATH = "/items";
    private static final String ISBN_PATH = "/randomisbn";

    public static void execute() {
        try {
            System.out.println("BUSCANDO TODOS OS ITEMS...");
            getAllItems();

            System.out.println("\nGERANDO ISBN ALEATÓRIO...");
            final var isbn13Response = generateRandomIsbn();

            System.out.println("\nCRIANDO NOVO ITEM...");
            final var createItemResponse = createNewItem(isbn13Response.getBody());

            System.out.println("\nATUALIZANDO ITEM CRIADO...");
            final var updatedItemResponse = updateItem(createItemResponse.getBody());

            System.out.println("\nREMOVENDO ITEM...");
            removeItem(updatedItemResponse.getBody().id());
        } catch (Exception e) {
            System.err.println("Erro ao executar exercise 12: " + e.getMessage());
        }
    }

    private static APIResponse<Items> getAllItems() throws IOException {
        final var getAllResponse = HttpClient.get(URL + ITEMS_PATH, Items.class);
        printStatusCode(getAllResponse);

        System.out.println("Lista de itens:");
        getAllResponse.getBody().items().forEach(item -> {
            System.out.println(item.toString());
        });

        return getAllResponse;
    }

    private static APIResponse<String> generateRandomIsbn() throws IOException {
        final var getIsbnResponse = HttpClient.get(URL + ISBN_PATH, String.class);
        printStatusCode(getIsbnResponse);

        System.out.println("ISBN gerado: " + getIsbnResponse.getBody());

        return getIsbnResponse;
    }

    private static APIResponse<Item> createNewItem(String isbn13) throws IOException {
        final var newItem = new Item(
                null,
                "book",
                isbn13,
                19.99,
                10);

        final var response = HttpClient.post(URL + ITEMS_PATH, newItem, Item.class);
        printStatusCode(response);

        System.out.println("Novo item criado: ");
        System.out.println(response.getBody().toString());

        return response;
    }

    private static APIResponse<Item> updateItem(Item oldItem) throws IOException {
        final var updatedItem = new Item(
                oldItem.id(),
                oldItem.type(),
                oldItem.isbn13(),
                1.99,
                100);

        final var fullURL = URL + ITEMS_PATH + "/" + oldItem.id();
        final var response = HttpClient.put(fullURL, updatedItem, Item.class);
        printStatusCode(response);

        System.out.println("Item atualizado: ");
        System.out.println(response.getBody().toString());

        return response;
    }

    private static APIResponse<?> removeItem(Integer id) throws IOException {
        if (id == null) {
            throw new IllegalArgumentException("ID do item não pode ser nulo.");
        }

        final var fullURL = URL + ITEMS_PATH + "/" + id;
        final var response = HttpClient.delete(fullURL);
        printStatusCode(response);

        if (response.getStatusCode() != HttpURLConnection.HTTP_OK) {
            System.out.println("Erro: " + response.getErrorMessage());
        } else {
            System.out.println("Item removido com sucesso.");
        }

        return response;
    }

    private static void printStatusCode(APIResponse<?> response) {
        final var statusCode = response.getStatusCode();
        System.out.println("Status code: " + statusCode);
    }
}
