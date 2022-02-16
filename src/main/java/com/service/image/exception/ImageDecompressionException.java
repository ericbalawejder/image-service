package com.service.image.exception;

public class ImageDecompressionException extends RuntimeException {

    public ImageDecompressionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageDecompressionException(String message) {
        super(message);
    }

    public ImageDecompressionException(Throwable cause) {
        super(cause);
    }

    public ImageDecompressionException() {
        super("image failed to decompress");
    }

}
