package com.jrpolesi.http;

public class APIResponse {
    private final int statusCode;
    private final String body;

    public APIResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }
}
