package com.backend.wavault.api;

import com.backend.wavault.model.request.LoginRequest;
import com.backend.wavault.model.request.RegisterRequest;
import com.backend.wavault.model.response.APIResponse;
import com.backend.wavault.service.token.TokenService;
import com.backend.wavault.service.user.AppUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/account")
public class UserController {

    private final AppUserService userService;
    private final TokenService tokenService;

    @PostMapping("/registration")
    public ResponseEntity<APIResponse> createAccount(@RequestBody RegisterRequest request) throws IOException {
        request.validateRegistrationRequest();
        APIResponse response = userService.createAccount(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/registration/verify")
    public ResponseEntity<APIResponse> validateConfirmationEmailToken(@RequestParam("t") String token, HttpServletRequest request){
        APIResponse response = (tokenService.validateConfirmationEmailToken(token, request));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/registration/resend-link")
    public ResponseEntity<APIResponse> sendNewConfirmationLink(@RequestParam String email, HttpServletRequest request){
        APIResponse response = tokenService.sendNewConfirmationLink(email, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse> authenticateLogin(@RequestBody LoginRequest request){
        request.validateLoginRequest();
        APIResponse response = userService.authenticateLogin(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
