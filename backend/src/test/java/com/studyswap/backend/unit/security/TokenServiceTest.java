package com.studyswap.backend.unit.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.studyswap.backend.model.Role;
import com.studyswap.backend.model.User;
import com.studyswap.backend.security.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    private TokenService tokenService;
    private User testUser;
    private final String testSecret = "my-test-secret-key-that-is-long-enough-for-hmac256";

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        
        try {
            Field secretField = TokenService.class.getDeclaredField("secret");
            secretField.setAccessible(true); // Torna o campo privado acessível
            ReflectionUtils.setField(secretField, tokenService, testSecret); // Define o valor
        } catch (NoSuchFieldException e) {
            fail("O teste falhou porque não encontrou o campo 'secret' na TokenService.", e);
        }

        testUser = new User("Test User", "test@example.com", "password", Role.STUDENT);
        testUser.setId(1L);
    }

    // ---------------------- generateToken ----------------------

    @Test
    void testGenerateToken_ShouldReturnValidJWT() {
        // Act
        String token = tokenService.generateToken(testUser);

        // Assert
        assertNotNull(token);

        DecodedJWT decodedJWT = JWT.decode(token);
        assertEquals("StudySwap", decodedJWT.getIssuer());
        assertEquals("test@example.com", decodedJWT.getSubject());
        assertEquals(1L, decodedJWT.getClaim("userId").asLong());
    }

    // ---------------------- validateToken ----------------------

    @Test
    void testValidateToken_WithValidToken_ShouldReturnSubject() {
        // Arrange
        String validToken = tokenService.generateToken(testUser);

        // Act
        String subject = tokenService.validateToken(validToken);

        // Assert
        assertEquals(testUser.getUsername(), subject);
    }
}