package com.backend.wavault.service.token;

import com.backend.wavault.mail.registration.OnRegistrationEvent;
import com.backend.wavault.model.dao.token.TokenRepository;
import com.backend.wavault.model.dao.user.AppUserRepository;
import com.backend.wavault.model.entity.AppUser;
import com.backend.wavault.model.entity.JwtToken;
import com.backend.wavault.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService{

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


    private Date calculateExpiryDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, UserUtils.TOEKN_EXPIRY_DATE);
        return new Date(cal.getTime().getTime());
    }
}
