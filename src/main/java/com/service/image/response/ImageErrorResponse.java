package com.service.image.response;

import org.springframework.http.HttpStatus;

public record ImageErrorResponse(HttpStatus status, String message, long timeStamp) {
}
