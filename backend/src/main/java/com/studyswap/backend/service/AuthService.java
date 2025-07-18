package com.studyswap.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.studyswap.backend.dto.UserRegistrationDTO;
import com.studyswap.backend.model.User;
import com.studyswap.backend.repository.UserRepository;

@Service
public class AuthService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        return this.userRepository.findByEmail(username);
    }

    public User register(UserRegistrationDTO userDTO){
        UserDetails userDetails = this.userRepository.findByEmail(userDTO.getEmail());
        if(userDetails != null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Email já cadastrado");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(userDTO.getPassword());

        User newUser = new User(userDTO.getName(), userDTO.getEmail(), encryptedPassword, userDTO.getRole());
        return this.userRepository.save(newUser);
    }
}
