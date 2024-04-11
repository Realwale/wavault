package com.backend.wavault.api;

import com.backend.wavault.model.request.RegisterRequest;
import com.backend.wavault.model.response.APIResponse;
import com.backend.wavault.service.user.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final AppUserService userService;

    @PostMapping("/account/registration")
    public ResponseEntity<APIResponse> createAccount(@RequestBody RegisterRequest request) throws IOException {
        request.validateRegistrationRequest();
        APIResponse response = userService.createAccount(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
