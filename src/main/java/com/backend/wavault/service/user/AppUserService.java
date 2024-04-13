package com.backend.wavault.service.user;

import com.backend.wavault.model.request.LoginRequest;
import com.backend.wavault.model.request.RegisterRequest;
import com.backend.wavault.model.response.APIResponse;

import java.io.IOException;

public interface AppUserService {

    APIResponse createAccount(RegisterRequest request) throws IOException;

    APIResponse authenticateLogin(LoginRequest request);
}
