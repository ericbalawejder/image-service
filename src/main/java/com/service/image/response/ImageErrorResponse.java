package com.service.image.response;

public record ImageErrorResponse(int status, String message, long timeStamp) {
}
