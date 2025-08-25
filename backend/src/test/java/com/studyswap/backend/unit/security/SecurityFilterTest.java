package com.studyswap.backend.unit.security;

import com.studyswap.backend.model.Role;
import com.studyswap.backend.model.User;
import com.studyswap.backend.repository.UserRepository;
import com.studyswap.backend.security.SecurityFilter;
import com.studyswap.backend.security.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityFilterTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private SecurityFilter securityFilter;

    private User testUser;

    @BeforeEach
    void setUp() {
        this.securityFilter = new SecurityFilter(tokenService, userRepository);

        testUser = new User("Test User", "test@example.com", "password", Role.STUDENT);
        testUser.setId(1L);

        SecurityContextHolder.clearContext();
    }

    // ---------------------- Teste de comportamento do filtro ----------------------

    @ParameterizedTest
    @ValueSource(strings = {
        "/auth/login",
        "/auth/register",
        "/uploads/file.png",
        "/images/logo.png"
    })
    void testDoFilter_ForPublicPaths_ShouldSkipAuthentication(String uri) throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn(uri);

        securityFilter.doFilter(request, response, filterChain);

        verify(tokenService, never()).validateToken(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testDoFilter_WithValidToken_ShouldAuthenticateUser() throws ServletException, IOException {
        String token = "valid-jwt-token";
        String userEmail = "test@example.com";
        when(request.getRequestURI()).thenReturn("/api/materials");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(tokenService.validateToken(token)).thenReturn(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(testUser));

        securityFilter.doFilter(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(userEmail, SecurityContextHolder.getContext().getAuthentication().getName());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilter_WithInvalidToken_ShouldNotAuthenticate() throws ServletException, IOException {
        String token = "invalid-jwt-token";
        when(request.getRequestURI()).thenReturn("/api/materials");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(tokenService.validateToken(token)).thenReturn("");

        securityFilter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    // Unificação dos 3 testes em 1 parametrizado
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "Token abcdef"})
    void testDoFilter_WithMissingOrMalformedToken_ShouldNotAuthenticate(String headerValue) throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/materials");
        when(request.getHeader("Authorization")).thenReturn(headerValue);

        securityFilter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
