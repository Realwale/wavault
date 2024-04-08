package com.backend.wavault.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "jwt_token")
public class JwtToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "token", unique = true)
    private String token;
    @Column(unique = true)
    private String refreshToken;
    private boolean isExpired;
    private boolean isRevoked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private AppUser user;

    @Column(name = "expiry_date")
    private Date expiryDate;

    private Date generatedAt;

    private Date refreshedAt;
}