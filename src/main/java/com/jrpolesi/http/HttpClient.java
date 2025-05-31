package com.jrpolesi.http;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

public class HttpClient {
    private static final Gson gson = new Gson();

    public static <T> APIResponse<T> get(String url, Class<T> responseBodyOf) throws IOException {
        return get(url, null, responseBodyOf);
    }

    public static <T> APIResponse<T> get(String url, Map<String, String> queryParams, Class<T> responseBodyOf) throws IOException {
        if (Objects.nonNull(queryParams) && !queryParams.isEmpty()) {
            url = concatQueryParams(url, queryParams);
        }

        final var connection = createConnection(url, "GET");

        try {
            final var response = readResponse(connection, responseBodyOf);

            connection.disconnect();

            return new APIResponse<T>(
                    connection.getResponseCode(),
                    response
            );
        } catch (IOException e) {
            connection.disconnect();

            final var errorResponse = handlerError(connection.getResponseCode());
            if (Objects.nonNull(errorResponse)) {
                return (APIResponse<T>) errorResponse;
            }

            throw e;
        }
    }

    public static <T> APIResponse<T> post(String url, Object body, Class<T> responseBodyOf) throws IOException {
        final var connection = createConnection(url, "POST");
        connection.setDoOutput(true);

        setBody(connection, body);

        try {
            final var response = readResponse(connection, responseBodyOf);

            connection.disconnect();

            return new APIResponse<T>(
                    connection.getResponseCode(),
                    response
            );
        } catch (IOException e) {
            connection.disconnect();
            throw e;
        }
    }

    public static <T> APIResponse<T> put(String url, Object body, Class<T> responseBodyOf) throws IOException {
        final var connection = createConnection(url, "PUT");
        connection.setDoOutput(true);

        setBody(connection, body);

        try {
            final var response = readResponse(connection, responseBodyOf);

            connection.disconnect();

            return new APIResponse<T>(
                    connection.getResponseCode(),
                    response
            );
        } catch (IOException e) {
            connection.disconnect();
            throw e;
        }
    }

    public static APIResponse<Object> delete(String url) throws IOException {
        return delete(url, null);
    }

    public static <T> APIResponse<T> delete(String url, Class<T> responseBodyOf) throws IOException {
        final var connection = createConnection(url, "DELETE");

        try {
            final var response = readResponse(connection, responseBodyOf);

            connection.disconnect();

            return new APIResponse<T>(
                    connection.getResponseCode(),
                    response
            );
        } catch (IOException e) {
            connection.disconnect();

            final var errorResponse = handlerError(connection.getResponseCode());
            if (Objects.nonNull(errorResponse)) {
                return (APIResponse<T>) errorResponse;
            }

            throw e;
        }
    }

    private static HttpURLConnection createConnection(String url, String method) {
        try {
            final var urlObject = new URI(url).toURL();
            final var connection = (HttpURLConnection) urlObject.openConnection();

            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            return connection;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar conexão HTTP", e);
        }
    }

    private static <T> T readResponse(HttpURLConnection connection, Class<T> responseBodyOf) throws IOException {
        System.out.println("Request URL: " + connection.getRequestMethod() + " - " + connection.getURL());

        final var in = new BufferedReader(
                new InputStreamReader(connection.getInputStream())
        );
        final var response = new StringBuilder();

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();

        if (Objects.isNull(responseBodyOf)) {
            return null;
        } else {
            return gson.fromJson(response.toString(), responseBodyOf);
        }
    }

    private static String concatQueryParams(String url, Map<String, String> queryParams) {
        final var params = queryParams.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .reduce((a, b) -> a + "&" + b)
                .orElse("");

        return url + "?" + params;
    }

    private static void setBody(HttpURLConnection connection, Object body) throws IOException {
        if (Objects.isNull(body)) {
            return;
        }

        final var bodyJson = gson.toJson(body);

        System.out.println("Request Body: " + bodyJson);

        final var outputStream = connection.getOutputStream();
        final var bodyBytes = bodyJson.getBytes(StandardCharsets.UTF_8);
        outputStream.write(bodyBytes);
    }

    private static <T> APIResponse<T> handlerError(int statusCode) {
        if (statusCode == HttpURLConnection.HTTP_NOT_FOUND) {
            return new APIResponse<T>(HttpURLConnection.HTTP_NOT_FOUND, "Entidade não encontrada");
        }

        return null;
    }
}
