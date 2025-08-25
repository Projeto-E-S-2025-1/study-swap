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
    private final String testSecret = "my-test-secret-key-that-is-long-enough";

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        try {
            Field secretField = TokenService.class.getDeclaredField("secret");
            secretField.setAccessible(true);
            ReflectionUtils.setField(secretField, tokenService, testSecret);
        } catch (NoSuchFieldException e) {
            fail("Não foi possível encontrar o campo 'secret' na classe TokenService.", e);
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
        assertTrue(decodedJWT.getExpiresAt().after(new Date()));
    }

    @Test
    void testGenerateToken_WithNullSecret_ShouldThrowRuntimeException() {
        // Arrange
        TokenService serviceWithNullSecret = new TokenService();

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            serviceWithNullSecret.generateToken(testUser);
        });
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

    @Test
    void testValidateToken_WithInvalidSignature_ShouldReturnEmptyString() {
        // Arrange
        String tokenWithWrongSecret = JWT.create()
                .withIssuer("StudySwap")
                .withSubject(testUser.getUsername())
                .sign(Algorithm.HMAC256("another-secret"));

        // Act
        String subject = tokenService.validateToken(tokenWithWrongSecret);

        // Assert
        assertEquals("", subject);
    }

    @Test
    void testValidateToken_WithExpiredToken_ShouldReturnEmptyString() {
        // Arrange
        String expiredToken = JWT.create()
                .withIssuer("StudySwap")
                .withSubject(testUser.getUsername())
                .withExpiresAt(Instant.now().minusSeconds(10)) // Expira 10 segundos no passado
                .sign(Algorithm.HMAC256(testSecret));

        // Act
        String subject = tokenService.validateToken(expiredToken);

        // Assert
        assertEquals("", subject);
    }

    @Test
    void testValidateToken_WithWrongIssuer_ShouldReturnEmptyString() {
        // Arrange
        String tokenWithWrongIssuer = JWT.create()
                .withIssuer("AnotherApp")
                .withSubject(testUser.getUsername())
                .withExpiresAt(Instant.now().plusSeconds(10))
                .sign(Algorithm.HMAC256(testSecret));

        // Act
        String subject = tokenService.validateToken(tokenWithWrongIssuer);

        // Assert
        assertEquals("", subject);
    }
}