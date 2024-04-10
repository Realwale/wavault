package com.backend.wavault.utils;


import com.backend.wavault.model.entity.AppUser;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtils {

    public static final int TOEKN_EXPIRY_DATE = 60 * 24;

    public static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,6}$";

    protected AppUser getUserFromContext(){
        return (AppUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
    }

    protected String getUserName(){
        return SecurityContextHolder.getContext().getAuthentication()
                .getName();
    }
}