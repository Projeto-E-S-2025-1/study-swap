//study-swap/backend/src/test/unity/app/services/autenticacao
package com.studyswap.api;

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
public class AuthControllerLoginTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @Mock
    private AuthService authService; // Pode ser útil em outros testes, mas não neste de login.

    @InjectMocks
    private AuthController authController;

    private UserLoginDTO userLoginDTO;
    private User userPrincipal;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        // Prepara os dados de teste
        userLoginDTO = new UserLoginDTO("test@example.com", "password123");
        userPrincipal = new User("Test User", "test@example.com", "encodedPassword", null);

        // Prepara o stub de autenticação
        authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null);

        // Define o comportamento dos stubs (o que eles fazem quando são chamados)
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenService.generateToken(any(User.class)))
                .thenReturn("mocked-jwt-token-123");
    }

    @Test
    void testLogin_Success() {
        // 1. Executa o método que queremos testar
        ResponseEntity<LoginResponseDTO> response = authController.login(userLoginDTO);

        // 2. Verifica se o resultado está correto
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("mocked-jwt-token-123", response.getBody().getToken());
    }
}