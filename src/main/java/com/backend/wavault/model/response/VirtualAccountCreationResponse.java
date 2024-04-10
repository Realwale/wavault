package com.backend.wavault.model.response;

import com.backend.wavault.model.flutterwave.FLWData;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VirtualAccountCreationResponse {
    @JsonProperty("status")
    private String status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private FLWData data;
}
