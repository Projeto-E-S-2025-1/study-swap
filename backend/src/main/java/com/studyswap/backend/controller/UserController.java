package com.studyswap.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.studyswap.backend.dto.MaterialResponseDTO;
import com.studyswap.backend.dto.UserResponseDTO;
import com.studyswap.backend.dto.UserUpdateDTO;
import com.studyswap.backend.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET perfil do usuário
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserProfile(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserProfile(id));
    }

    // PUT atualizar próprio perfil
    @PutMapping(value = "/me", consumes = {"multipart/form-data"})
    public ResponseEntity<UserResponseDTO> updateProfile(
            @RequestPart("userDTO") UserUpdateDTO userDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        return ResponseEntity.ok(userService.updateProfile(userDTO, file));
    }

    @PutMapping("/favorites/{id}")
    public ResponseEntity<Void> favoriteMaterial (@PathVariable("id") Long id_Material){
    	userService.favoriteMaterial(id_Material);
    	return  ResponseEntity.noContent().build();
    }
    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> unfavoriteMaterial (@PathVariable("id") Long id_Material){
    	userService.unfavoriteMaterial(id_Material);
    	return ResponseEntity.noContent().build();
    }

}
