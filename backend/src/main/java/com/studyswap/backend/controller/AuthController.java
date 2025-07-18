package com.studyswap.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studyswap.backend.dto.LoginResponseDTO;
import com.studyswap.backend.dto.UserLoginDTO;
import com.studyswap.backend.dto.UserRegistrationDTO;
import com.studyswap.backend.model.User;
import com.studyswap.backend.security.TokenService;
import com.studyswap.backend.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final AuthService authService;

    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager, 
        AuthService authService, TokenService tokenService) {
            this.authenticationManager = authenticationManager;
            this.authService = authService;
            this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid UserLoginDTO userLoginDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(
            userLoginDTO.getEmail(), userLoginDTO.getPassword());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        String token = this.tokenService.generateToken((User) auth.getPrincipal());
        
        return ResponseEntity.status(HttpStatus.OK).body(new LoginResponseDTO(token));
    }
    
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserRegistrationDTO userRegistrationDTO) {
        User registeredUser = this.authService.register(userRegistrationDTO);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }
    
}
