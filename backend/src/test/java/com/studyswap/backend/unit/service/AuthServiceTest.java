package com.studyswap.backend.unit.service;

import com.studyswap.backend.dto.UserRegistrationDTO;
import com.studyswap.backend.model.User;
import com.studyswap.backend.repository.UserRepository;
import com.studyswap.backend.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private UserRegistrationDTO registrationDTO;

    @BeforeEach
    void setUp() {
        // Usuário para testes
        testUser = new User("Test User", "test@example.com", "$2a$10$...", null); // Senha encriptada
        testUser.setId(1L);

        // DTO para registro
        registrationDTO = new UserRegistrationDTO();
        registrationDTO.setName("New User");
        registrationDTO.setEmail("newuser@example.com");
        registrationDTO.setPassword("plainPassword123");
        registrationDTO.setRole(null);
    }

    // ---------------------- loadUserByUsername ----------------------

    @Test
    void testLoadUserByUsername_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = authService.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testLoadUserByUsername_NotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            authService.loadUserByUsername("notfound@example.com");
        });
    }

    // ---------------------- register ----------------------

    @Test
    void testRegister_Success() {
        when(userRepository.findByEmail(registrationDTO.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User userToSave = invocation.getArgument(0);
            userToSave.setId(2L); // Simula o salvamento e atribuição de ID
            return userToSave;
        });

        User newUser = authService.register(registrationDTO);

        assertNotNull(newUser);
        assertEquals(registrationDTO.getName(), newUser.getName());
        assertEquals(registrationDTO.getEmail(), newUser.getEmail());
        assertNotEquals(registrationDTO.getPassword(), newUser.getPassword()); // Verifica se a senha foi encriptada
        verify(userRepository, times(1)).findByEmail(registrationDTO.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegister_EmailAlreadyExists() {
        when(userRepository.findByEmail(registrationDTO.getEmail())).thenReturn(Optional.of(testUser));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            authService.register(registrationDTO);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("400 BAD_REQUEST \"Email já cadastrado\"", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    // ---------------------- getAuthenticatedUser ----------------------

    @Test
    void testGetAuthenticatedUser_Success() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            // Arrange
            mockedContext.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn(testUser.getEmail());
            when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

            // Act
            User authenticatedUser = authService.getAuthenticatedUser();

            // Assert
            assertNotNull(authenticatedUser);
            assertEquals(testUser.getId(), authenticatedUser.getId());
            assertEquals(testUser.getEmail(), authenticatedUser.getEmail());
        }
    }

    @Test
    void testGetAuthenticatedUser_NotFound() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            // Arrange
            mockedContext.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn("notfound@example.com");
            when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

            // Act & Assert
            ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
                authService.getAuthenticatedUser();
            });

            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
            assertEquals("404 NOT_FOUND \"Usuário não encontrado\"", exception.getMessage());
        }
    }
}