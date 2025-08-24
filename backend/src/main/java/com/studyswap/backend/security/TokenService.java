package com.studyswap.backend.security;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.studyswap.backend.model.User;

@Service
public class TokenService {

    @Value("${api.security.auth.token.secret}")
    private String secret;
    
    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret);
            
            return JWT.create()
                .withIssuer("StudySwap")
                .withSubject(user.getUsername())
                .withClaim("userId", user.getId())
                .withExpiresAt(this.getExpirationAt())
                .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error creating token", e);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret);
            return JWT.require(algorithm)
                    .withIssuer("StudySwap")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            return "";
        }
    }

    private Instant getExpirationAt() {
        return Instant.now().plusSeconds(86400);
    }
}
