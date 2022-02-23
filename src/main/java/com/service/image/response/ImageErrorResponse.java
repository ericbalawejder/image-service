package com.service.image.response;

import org.springframework.http.HttpStatus;

public record ImageErrorResponse(HttpStatus httpStatus, String message, long timestamp) {
}
