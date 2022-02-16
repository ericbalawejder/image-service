package com.service.image.exception;

public class ImageSizeException extends RuntimeException {

    public ImageSizeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageSizeException(String message) {
        super(message);
    }

    public ImageSizeException(Throwable cause) {
        super(cause);
    }

    public ImageSizeException() {
        super("image size is greater than 100MB (100_000_000 bytes)");
    }

}
