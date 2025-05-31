package com.jrpolesi.http;

public class APIResponse<T> {
    private final int statusCode;
    private final T body;
    private final String errorMessage;

    private APIResponse(int statusCode, T body, String errorMessage) {
        this.statusCode = statusCode;
        this.body = body;
        this.errorMessage = errorMessage;
    }

    public APIResponse(int statusCode, T body) {
        this(statusCode, body, null);
    }

    public APIResponse(int statusCode, String errorMessage) {
        this(statusCode, null, errorMessage);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public T getBody() {
        return body;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
