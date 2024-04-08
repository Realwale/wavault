package com.backend.wavault.exception;

/**
 * Represents Unprocessable Entity Exception.
 */
public class UnprocessableEntityException extends RuntimeException {

    public UnprocessableEntityException(String message) {
        super(message);
    }
}
