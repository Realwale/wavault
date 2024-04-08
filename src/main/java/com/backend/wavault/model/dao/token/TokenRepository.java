package com.backend.wavault.model.dao.token;

import com.backend.wavault.model.entity.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<JwtToken, Long> {
}
