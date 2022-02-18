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

import java.security.NoSuchAlgorithmException;
import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class ImageExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ImageErrorResponse> handleImageRepositoryException(ImageRepositoryException exc) {
        final ImageErrorResponse error = new ImageErrorResponse(
                HttpStatus.BAD_REQUEST, exc.getMessage(), System.currentTimeMillis());

        return new ResponseEntity<>(error, error.status());
    }

    @ExceptionHandler
    public ResponseEntity<ImageErrorResponse> handleImageSizeException(ImageSizeException exc) {
        final ImageErrorResponse error = new ImageErrorResponse(
                HttpStatus.PAYLOAD_TOO_LARGE, exc.getMessage(), System.currentTimeMillis());

        return new ResponseEntity<>(error, error.status());
    }

    @ExceptionHandler
    public ResponseEntity<ImageErrorResponse> handleNoSuchAlgorithmException(NoSuchAlgorithmException exc) {
        final ImageErrorResponse error = new ImageErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, exc.getMessage(), System.currentTimeMillis());

        return new ResponseEntity<>(error, error.status());
    }

    @ExceptionHandler
    public ResponseEntity<ImageErrorResponse> handleCompressionException(ImageCompressionException exc) {
        final ImageErrorResponse error = new ImageErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, exc.getMessage(), System.currentTimeMillis());

        return new ResponseEntity<>(error, error.status());
    }

    @ExceptionHandler
    public ResponseEntity<ImageErrorResponse> handleDecompressionException(ImageDecompressionException exc) {
        final ImageErrorResponse error = new ImageErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, exc.getMessage(), System.currentTimeMillis());

        return new ResponseEntity<>(error, error.status());
    }

    @ExceptionHandler
    public ResponseEntity<ImageErrorResponse> handleDuplicateImageUpload(
            SQLIntegrityConstraintViolationException exc) {
        final ImageErrorResponse error = new ImageErrorResponse(
                HttpStatus.BAD_REQUEST, "image is already on file", System.currentTimeMillis());

        return new ResponseEntity<>(error, error.status());
    }

    @ExceptionHandler
    public ResponseEntity<ImageErrorResponse> handleAll(Exception exc) {
        final String message = "¯\\_(ツ)_/¯";
        final ImageErrorResponse error = new ImageErrorResponse(
                HttpStatus.BAD_REQUEST, message, System.currentTimeMillis());

        return new ResponseEntity<>(error, error.status());
    }

}
