package com.backend.wavault.model.response;

import com.backend.wavault.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@JsonPropertyOrder(value = {"statusCode", "hasError", "message", "time", "data"})
public class APIResponse {

    private boolean hasError;
    private int statusCode;
    private String message;
    private String time;
    private Object data;

    public APIResponse(boolean hasError, int statusCode, String message, String time, Object data) {
        this.hasError = hasError;
        this.statusCode = statusCode;
        this.message = message;
        this.time = DateUtils.saveDate(LocalDateTime.now());
        this.data = data;
    }
}