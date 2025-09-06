package com.studyswap.backend.unit.controller;

import com.studyswap.backend.controller.UserController;
import com.studyswap.backend.dto.UserResponseDTO;
import com.studyswap.backend.dto.UserUpdateDTO;
import com.studyswap.backend.model.Role;
import com.studyswap.backend.security.TokenService;
import com.studyswap.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private UserController userController;

    @Test
    void testGetUserProfile() {
        UserResponseDTO responseDTO = new UserResponseDTO(
                1L, "João", "/images/default.png", "Música", Role.STUDENT
        );

        Mockito.when(userService.getUserProfile(1L)).thenReturn(responseDTO);

        ResponseEntity<UserResponseDTO> response = userController.getUserProfile(1L);
        UserResponseDTO result = response.getBody();

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("João", result.getName());
        assertEquals("/images/default.png", result.getPhotoUrl());
        assertEquals("Música", result.getInterests());
        assertEquals(Role.STUDENT, result.getRole());
    }

    @Test
    void testUpdateProfileWithFile() throws Exception {
        UserResponseDTO responseDTO = new UserResponseDTO(
                1L, "Maria", "/uploads/foto.png", "Leitura", Role.STUDENT
        );

        Mockito.when(userService.updateProfile(any(UserUpdateDTO.class), any())).thenReturn(responseDTO);

        MockMultipartFile filePart = new MockMultipartFile(
                "file", "foto.png", "image/png", "fake image".getBytes()
        );
        UserUpdateDTO userDTO = new UserUpdateDTO();

        ResponseEntity<UserResponseDTO> response = userController.updateProfile(userDTO, filePart);
        UserResponseDTO result = response.getBody();

        assertNotNull(result);
        assertEquals("Maria", result.getName());
        assertEquals("/uploads/foto.png", result.getPhotoUrl());
    }

    @Test
    void testUpdateProfileWithoutFile() throws Exception {
        UserResponseDTO responseDTO = new UserResponseDTO(
                1L, "Carlos", "/images/default.png", "Esportes", Role.STUDENT
        );

        Mockito.when(userService.updateProfile(any(UserUpdateDTO.class), eq(null))).thenReturn(responseDTO);

        UserUpdateDTO userDTO = new UserUpdateDTO();

        ResponseEntity<UserResponseDTO> response = userController.updateProfile(userDTO, null);
        UserResponseDTO result = response.getBody();

        assertNotNull(result);
        assertEquals("Carlos", result.getName());
        assertEquals("/images/default.png", result.getPhotoUrl());
    }
}
