package com.backend.wavault.model.dao.token;

import com.backend.wavault.model.entity.AppUser;
import com.backend.wavault.model.entity.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<JwtToken, Long> {
    Optional<JwtToken> findByToken(String token);

    boolean existsByUser(AppUser appUser);

    JwtToken findByUser(AppUser appUser);
}
