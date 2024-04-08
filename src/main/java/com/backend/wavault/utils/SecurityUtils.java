package com.backend.wavault.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Utility class for security operations.
 */
public final class SecurityUtils {

    private static final Logger logger = LoggerFactory.getLogger(SecurityUtils.class);

    /**
     * Retrieves the current authenticated user's details.
     *
     * @return UserDetails of the authenticated user, or null if no user is authenticated.
     */
    public static UserDetails getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            logger.debug("No authentication found in security context.");
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return (UserDetails) principal;
        }
        logger.debug("Principal is not an instance of UserDetails: {}", principal);
        return null;
    }

    private SecurityUtils() {
        // Private constructor to prevent instantiation
    }
}
