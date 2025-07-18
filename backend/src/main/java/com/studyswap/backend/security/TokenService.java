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

    @Value("${api.security.token.secret}")
    private String secret;
    
    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret);

            String token = JWT.create()
                .withIssuer("StudySwap")
                .withSubject(user.getName())
                .withExpiresAt(this.getExpirationAt())
                .sign(algorithm);

            return token;
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error creating token", e);
        }
    }

    private Instant getExpirationAt() {
        return Instant.now().plusSeconds(86400);
    }
}
