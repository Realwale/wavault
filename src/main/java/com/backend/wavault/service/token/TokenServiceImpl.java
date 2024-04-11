package com.backend.wavault.service.token;

import com.backend.wavault.exception.InvalidArgumentException;
import com.backend.wavault.exception.ResourceAlreadyExistsException;
import com.backend.wavault.exception.ResourceNotFoundException;
import com.backend.wavault.mail.registration.OnRegistrationEvent;
import com.backend.wavault.model.dao.token.TokenRepository;
import com.backend.wavault.model.dao.user.AppUserRepository;
import com.backend.wavault.model.entity.AppUser;
import com.backend.wavault.model.entity.JwtToken;
import com.backend.wavault.model.response.APIResponse;
import com.backend.wavault.utils.EmailUtils;
import com.backend.wavault.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final AppUserRepository userRepository;

    @Override
    public void saveConfirmationToken(AppUser user) {
        String token = UUID.randomUUID().toString();
        JwtToken jwtToken = JwtToken.builder()
                .token(token)
                .user(user)
                .expiryDate(calculateExpiryDate())
                .generatedAt(Date.from(Instant.now()))
                .build();

        tokenRepository.save(jwtToken);
        eventPublisher.publishEvent(new OnRegistrationEvent(user, token));

    }


    @Override
    public APIResponse validateConfirmationEmailToken(String token, HttpServletRequest request) {
        JwtToken confirmationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Null confirmation link"));

        AppUser user = confirmationToken.getUser();

        if (Objects.isNull(user)) {
            throw new ResourceNotFoundException("User not found with email");
        }

        if (user.isVerified()) {
            throw new ResourceAlreadyExistsException("Your account is already verified. Proceed to login.");
        }

        if (confirmationToken.getExpiryDate().before(new Date())) {
            tokenRepository.delete(confirmationToken);
            throw new InvalidArgumentException("Token is expired. Please request a new verification link at: "
                    + EmailUtils.applicationUrl(request) + "/api/v1/account/new-verification-link?email=" + user.getEmail());
        }
        user.setVerified(true);
        userRepository.save(user);

        return APIResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .hasError(false)
                .message("Email successfully verified")
                .data(null)
                .build();
    }

    @Override
    public APIResponse sendNewConfirmationLink(String email, HttpServletRequest request) {

        AppUser appUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        if (appUser.isVerified()) {
            throw new ResourceAlreadyExistsException("Account already verified, Proceed to login");
        }

        if (tokenRepository.existsByUser(appUser)) {
            tokenRepository.delete(tokenRepository.findByUser(appUser));
        }
        eventPublisher.publishEvent(new OnRegistrationEvent(appUser, EmailUtils.applicationUrl(request)));

        return APIResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .hasError(false)
                .message("Please check your mail for a new verification link")
                .data(null)
                .build();
    }

    private Date calculateExpiryDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, UserUtils.TOEKN_EXPIRY_DATE);
        return new Date(cal.getTime().getTime());
    }


}
