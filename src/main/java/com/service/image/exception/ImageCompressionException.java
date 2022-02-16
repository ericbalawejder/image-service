package com.service.image.exception;

public class ImageCompressionException extends RuntimeException {

    public ImageCompressionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageCompressionException(String message) {
        super(message);
    }

    public ImageCompressionException(Throwable cause) {
        super(cause);
    }

    public ImageCompressionException() {
        super("image failed to compress");
    }

}
