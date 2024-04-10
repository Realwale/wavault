package com.backend.wavault.service.token;

import com.backend.wavault.model.entity.AppUser;

public interface TokenService {

    void saveConfirmationToken(AppUser user);
}
