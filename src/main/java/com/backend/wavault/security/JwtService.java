package com.backend.wavault.security;


import com.backend.wavault.model.dao.token.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtService {

    private final TokenRepository tokenRepository;

    @Value("${jwt.expiration.access-token}")
    private long access_expiration;
    @Value("${jwt.expiration.refresh-token}")
    private long refresh_expiration;
    @Value("${jwt.secret-key}")
    private String secret_key;


    private Key generateSignInKey(){
        byte[] secretKeyInBytes = Decoders.BASE64.decode(secret_key);
        return Keys.hmacShaKeyFor(secretKeyInBytes);
    }

    public String generateToken(UserDetails userDetails){
        return getExtractedClaims(userDetails, new HashMap<>(), access_expiration);

    }

    public String generateRefreshToken(
            UserDetails userDetails
    ){
        return buildToken (new HashMap<>(), userDetails, refresh_expiration);
    }

    public String getExtractedClaims(
            UserDetails userDetails,
            Map<String, Object> extractClaim,
            long expiration){
        Set<String> role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        extractClaim.put("role", role);
        return buildToken(extractClaim, userDetails, expiration);
    }

    public String extractUsername(String token){
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(generateSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractSingleClaim(String token, Function<Claims, T> claimResolver){
        Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }


    private Date extractExpiration(String token){
        return extractSingleClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(generateSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

}