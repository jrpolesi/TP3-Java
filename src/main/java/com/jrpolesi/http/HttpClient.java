package com.jrpolesi.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;

public class HttpClient {
    public static APIResponse get(String url) throws IOException {
        final var connection = createConnection(url);

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        final var in = new BufferedReader(
                new InputStreamReader(connection.getInputStream())
        );

        String inputLine;
        final var response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();
        connection.disconnect();

        return new APIResponse(
                connection.getResponseCode(),
                response.toString()
        );
    }

    private static HttpURLConnection createConnection(String url) {
        try {
            final var urlObject = new URI(url).toURL();
            return (HttpURLConnection) urlObject.openConnection();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar conexão HTTP", e);
        }
    }
}
