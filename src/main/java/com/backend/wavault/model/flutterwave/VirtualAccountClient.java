package com.backend.wavault.model.flutterwave;


import com.backend.wavault.model.request.VirtualAccountCreationRequest;
import com.backend.wavault.model.response.APIResponse;
import com.backend.wavault.model.response.VirtualAccountCreationResponse;
import com.backend.wavault.utils.RestTemplateUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class VirtualAccountClient {

    private final RestTemplate restTemplate;
    private final RestTemplateUtils restTemplateUtils;



    public Object createVirtualAccount(VirtualAccountCreationRequest request) throws IOException {
        request.setAmount(100);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(request);
        HttpEntity<String> httpEntity = new HttpEntity<>(jsonString, restTemplateUtils.getHeaders());

        try {
            ResponseEntity<String> response = restTemplate.exchange(FlutterURLs.VIRTUAL_ACCOUNT_URL, HttpMethod.POST, httpEntity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                String responseBody = response.getBody();

                return objectMapper.readValue(responseBody, VirtualAccountCreationResponse.class);
            }
            return APIResponse.builder()
                    .hasError(true)
                    .message(response.getBody())
                    .statusCode(response.getStatusCode().value())
                    .data(null)
                    .build();
        } catch (RestClientException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
