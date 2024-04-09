package com.backend.wavault.constants;

public class UrlConstants {

    public static final String[] WHITE_LIST_URLS = new String[]{
            "/",
            "/api/v1/auth/**",
            "/v3/api-docs.yaml",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/api/v1/account/registration",
            "/api/v1/account/registration/verify",
            "/api/v1/account/registration/resend-link",
            "/api/v1/account/login"

    };

    public static final String[] ADMIN_ONLY_URLS = new String[]{

    };

    public static final String[] USER_ONLY_URLS = new String[]{

    };

    public static final String ADMIN_AUTHORITY = "ADMIN";
    public static final String USER_AUTHORITY = "USER";

}
