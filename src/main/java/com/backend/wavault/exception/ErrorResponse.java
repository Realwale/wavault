package com.backend.wavault.exception;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Data
public class ErrorResponse {

    private final List<String> errors;

    public ErrorResponse(String error) {
        this(error != null ? Collections.singletonList(error) : Collections.emptyList());
    }
}