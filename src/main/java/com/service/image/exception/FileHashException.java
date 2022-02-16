package com.service.image.exception;

public class FileHashException extends RuntimeException {

    public FileHashException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileHashException(String message) {
        super(message);
    }

    public FileHashException(Throwable cause) {
        super(cause);
    }

    public FileHashException() {
        super("failed to hash file");
    }

}
