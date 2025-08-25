package com.studyswap.backend.unit.security;

import com.studyswap.backend.security.SecurityConfig;
import com.studyswap.backend.security.SecurityFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Mock
    private SecurityFilter securityFilter;

    @InjectMocks
    private SecurityConfig securityConfig;

    @Test
    void testPasswordEncoder_ShouldReturnBCryptPasswordEncoder() {
        // Act
        PasswordEncoder result = securityConfig.passwordEncoder();

        // Assert
        assertNotNull(result);
        assertInstanceOf(BCryptPasswordEncoder.class, result);
    }

    @Test
    void testAuthenticationManager_ShouldReturnManagerFromConfig() throws Exception {
        // Arrange
        AuthenticationConfiguration authConfig = mock(AuthenticationConfiguration.class);
        AuthenticationManager authManager = mock(AuthenticationManager.class);
        when(authConfig.getAuthenticationManager()).thenReturn(authManager);

        // Act
        AuthenticationManager result = securityConfig.authenticationManager(authConfig);

        // Assert
        assertNotNull(result);
        assertEquals(authManager, result);
        verify(authConfig, times(1)).getAuthenticationManager();
    }
    
    @Test
    void testSecurityFilterChain_ShouldAddFilterBefore() throws Exception {
        // Arrange
        HttpSecurity httpSecurityMock = mock(HttpSecurity.class);

        when(httpSecurityMock.csrf(any())).thenReturn(httpSecurityMock);
        when(httpSecurityMock.cors(any())).thenReturn(httpSecurityMock);
        when(httpSecurityMock.sessionManagement(any())).thenReturn(httpSecurityMock);
        when(httpSecurityMock.authorizeHttpRequests(any())).thenReturn(httpSecurityMock);
        when(httpSecurityMock.addFilterBefore(any(), any())).thenReturn(httpSecurityMock);

        // Act
        securityConfig.securityFilterChain(httpSecurityMock);

        // Assert
        ArgumentCaptor<SecurityFilter> filterCaptor = ArgumentCaptor.forClass(SecurityFilter.class);
        
        verify(httpSecurityMock).addFilterBefore(filterCaptor.capture(), eq(UsernamePasswordAuthenticationFilter.class));
        
        assertEquals(securityFilter, filterCaptor.getValue());
        verify(httpSecurityMock).build();
    }
}