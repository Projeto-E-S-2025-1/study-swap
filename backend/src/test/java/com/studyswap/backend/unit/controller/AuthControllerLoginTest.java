//study-swap/backend/src/test/unity/app/services/autenticacao
package com.studyswap.backend.unit.controller;

import com.studyswap.backend.controller.AuthController;
import com.studyswap.backend.dto.LoginResponseDTO;
import com.studyswap.backend.dto.UserLoginDTO;
import com.studyswap.backend.model.User;
import com.studyswap.backend.security.TokenService;
import com.studyswap.backend.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerLoginTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @Mock
    private AuthService authService; 

    @InjectMocks
    private AuthController authController;

    private UserLoginDTO userLoginDTO;
    private User userPrincipal;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        userLoginDTO = new UserLoginDTO("test@example.com", "password123");
        userPrincipal = new User("Test User", "test@example.com", "encodedPassword", null);

        authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenService.generateToken(any(User.class)))
                .thenReturn("mocked-jwt-token-123");
    }

    @Test
    void testLogin_Success() {
        ResponseEntity<LoginResponseDTO> response = authController.login(userLoginDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        LoginResponseDTO body = response.getBody();
        assertNotNull(body);
        assertEquals("mocked-jwt-token-123", body.getToken());
    }
}