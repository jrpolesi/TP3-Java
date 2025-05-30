package com.jrpolesi.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;
import java.util.Objects;

public class HttpClient {
    public static APIResponse get(String url) throws IOException {
        return get(url, null);
    }

    public static APIResponse get(String url, Map<String, String> queryParams) throws IOException {
        if (Objects.nonNull(queryParams) && !queryParams.isEmpty()) {
            url = concatQueryParams(url, queryParams);
        }

        final var connection = createConnection(url);

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        try {
            final var response = readResponse(connection);

            connection.disconnect();

            return new APIResponse(
                    connection.getResponseCode(),
                    response
            );
        } catch (IOException e) {
            connection.disconnect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                return new APIResponse(HttpURLConnection.HTTP_NOT_FOUND, "Not Found");
            }

            throw e;
        }
    }

    private static HttpURLConnection createConnection(String url) {
        try {
            final var urlObject = new URI(url).toURL();
            return (HttpURLConnection) urlObject.openConnection();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar conexão HTTP", e);
        }
    }

    private static String readResponse(HttpURLConnection connection) throws IOException {
        final var in = new BufferedReader(
                new InputStreamReader(connection.getInputStream())

        );
        final var response = new StringBuilder();

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();
        return response.toString();
    }

    private static String concatQueryParams(String url, Map<String, String> queryParams) {
        final var params = queryParams.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .reduce((a, b) -> a + "&" + b)
                .orElse("");

        return url + "?" + params;
    }
}
