package com.backend.wavault.service.token;

import com.backend.wavault.model.entity.AppUser;
import com.backend.wavault.model.response.APIResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface TokenService {

    void saveConfirmationToken(AppUser user);

    APIResponse confirmEmail(String token, HttpServletRequest request);

    APIResponse sendNewConfirmationLink(String email, HttpServletRequest request);
}
