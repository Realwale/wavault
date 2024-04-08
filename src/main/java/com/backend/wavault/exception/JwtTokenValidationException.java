package com.backend.wavault.exception;

public class JwtTokenValidationException extends RuntimeException {

    public JwtTokenValidationException(String message) {
        super(message);
    }
}