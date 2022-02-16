package com.service.image.controller;

import com.service.image.exception.ImageCompressionException;
import com.service.image.exception.ImageDecompressionException;
import com.service.image.exception.ImageRepositoryException;
import com.service.image.exception.ImageSizeException;
import com.service.image.response.ImageErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ImageExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ImageErrorResponse> handleImageRepositoryException(ImageRepositoryException exc) {
        final ImageErrorResponse error = new ImageErrorResponse(
                HttpStatus.NO_CONTENT.value(), exc.getMessage(), System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ImageErrorResponse> handleImageSizeException(ImageSizeException exc) {
        final ImageErrorResponse error = new ImageErrorResponse(
                HttpStatus.PAYLOAD_TOO_LARGE.value(), exc.getMessage(), System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ImageErrorResponse> handleCompressionException(ImageCompressionException exc) {
        final ImageErrorResponse error = new ImageErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), exc.getMessage(), System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ImageErrorResponse> handleDecompressionException(ImageDecompressionException exc) {
        final ImageErrorResponse error = new ImageErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), exc.getMessage(), System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ImageErrorResponse> handleAll(Exception exc) {
        final String message = "¯\\_(ツ)_/¯";
        final ImageErrorResponse error = new ImageErrorResponse(
                HttpStatus.BAD_REQUEST.value(), message, System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
