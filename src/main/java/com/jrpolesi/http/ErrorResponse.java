package com.jrpolesi.http;

import java.util.List;

public record ErrorResponse(List<String> errorMessages) {
}
