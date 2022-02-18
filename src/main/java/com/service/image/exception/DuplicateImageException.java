package com.service.image.exception;

public class DuplicateImageException extends RuntimeException {

    public DuplicateImageException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateImageException(String message) {
        super(message);
    }

    public DuplicateImageException(Throwable cause) {
        super(cause);
    }

    public DuplicateImageException() {
        super("image is already on file");
    }

}
