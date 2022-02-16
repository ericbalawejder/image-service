package com.service.image.exception;

public class ImageRepositoryException extends RuntimeException {

    public ImageRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageRepositoryException(String message) {
        super(message);
    }

    public ImageRepositoryException(Throwable cause) {
        super(cause);
    }

    public ImageRepositoryException() {
        super("file name not found");
    }

}
