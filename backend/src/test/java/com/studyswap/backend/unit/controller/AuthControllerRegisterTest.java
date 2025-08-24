package com.studyswap.backend.unit.controller;

import com.studyswap.backend.controller.AuthController;
import com.studyswap.backend.dto.UserRegistrationDTO;
import com.studyswap.backend.model.Role;
import com.studyswap.backend.model.User;
import com.studyswap.backend.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerRegisterTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private UserRegistrationDTO registrationDTO;
    private User registeredUser;

    @BeforeEach
    void setUp() {
        registrationDTO = new UserRegistrationDTO("New User", "newuser@example.com", "password123", Role.STUDENT);
        registeredUser = new User("New User", "newuser@example.com", "encodedPassword", Role.STUDENT);
    }

    @Test
    void testRegister_Success() {
        when(authService.register(any(UserRegistrationDTO.class)))
                .thenReturn(registeredUser);

        ResponseEntity<User> response = authController.register(registrationDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("New User", response.getBody().getName());
        assertEquals("newuser@example.com", response.getBody().getEmail());
    }

    @Test
    void testRegister_EmailAlreadyExists() {
        when(authService.register(any(UserRegistrationDTO.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            authController.register(registrationDTO);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("400 BAD_REQUEST \"Email já cadastrado\"", exception.getMessage());
    }
}