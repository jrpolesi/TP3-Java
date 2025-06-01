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
        final var response = readResponse(connection, responseBodyOf);

        connection.disconnect();

        return response;
    }

    public static <T> APIResponse<T> post(String url, Object body, Class<T> responseBodyOf) throws IOException {
        final var connection = createConnection(url, "POST");
        connection.setDoOutput(true);

        setBody(connection, body);

        final var response = readResponse(connection, responseBodyOf);

        connection.disconnect();

        return response;
    }

    public static <T> APIResponse<T> put(String url, Object body, Class<T> responseBodyOf) throws IOException {
        final var connection = createConnection(url, "PUT");
        connection.setDoOutput(true);

        setBody(connection, body);

        final var response = readResponse(connection, responseBodyOf);

        connection.disconnect();

        return response;
    }

    public static APIResponse<Object> delete(String url) throws IOException {
        return delete(url, null);
    }

    public static <T> APIResponse<T> delete(String url, Class<T> responseBodyOf) throws IOException {
        final var connection = createConnection(url, "DELETE");
        final var response = readResponse(connection, responseBodyOf);

        connection.disconnect();

        return response;
    }

    public static OptionsResponse options(String url) throws IOException {
        final var connection = createConnection(url, "OPTIONS");
        connection.setDoOutput(true);

        final var allowMethods = connection.getHeaderField("Allow");
        final var statusCode = connection.getResponseCode();

        connection.disconnect();

        return new OptionsResponse(
                statusCode,
                allowMethods
        );
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

    private static <T> APIResponse<T> readResponse(HttpURLConnection connection, Class<T> responseBodyOf) throws IOException {
        System.out.println("Request URL: " + connection.getRequestMethod() + " - " + connection.getURL());

        final var statusCode = connection.getResponseCode();

        if (statusCode > 199 && statusCode < 300) {
            return readSuccessResponse(connection, responseBodyOf);
        }

        return readErrorResponse(connection);
    }

    private static <T> APIResponse<T> readSuccessResponse(HttpURLConnection connection, Class<T> responseBodyOf) throws IOException {
        final var inputStream = connection.getInputStream();

        final var in = new BufferedReader(
                new InputStreamReader(inputStream)
        );

        final var response = new StringBuilder();

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();

        final var statusCode = connection.getResponseCode();

        if (response.toString().isEmpty()) {
            return new APIResponse<>(
                    statusCode,
                    null
            );
        } else {
            final var body = gson.fromJson(response.toString(), responseBodyOf);
            return new APIResponse<T>(
                    statusCode,
                    body
            );
        }
    }

    private static <T> APIResponse<T> readErrorResponse(HttpURLConnection connection) throws IOException {
        final var inputStream = connection.getErrorStream();

        final var in = new BufferedReader(
                new InputStreamReader(inputStream)
        );

        final var response = new StringBuilder();

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();

        final var statusCode = connection.getResponseCode();

        if (response.toString().isEmpty()) {
            return new APIResponse<>(
                    statusCode,
                    null
            );
        } else {
            final var body = gson.fromJson(response.toString(), ErrorResponse.class);

            final var errorMessage = body.errorMessages().stream().reduce(
                    (a, b) -> a + "; " + b
            ).orElse("Erro desconhecido.") + ".";

            return new APIResponse<>(
                    statusCode,
                    errorMessage
            );
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
}
