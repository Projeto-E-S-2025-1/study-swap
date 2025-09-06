package com.studyswap.backend.unit.service;

import com.studyswap.backend.dto.UserResponseDTO;
import com.studyswap.backend.dto.UserUpdateDTO;
import com.studyswap.backend.model.Role;
import com.studyswap.backend.model.User;
import com.studyswap.backend.repository.UserRepository;
import com.studyswap.backend.service.AuthService;
import com.studyswap.backend.service.UserService;
import com.studyswap.backend.service.exception.FileStorageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private AuthService authService;
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        authService = mock(AuthService.class);
        userService = new UserService(userRepository, authService);

        user = new User();
        user.setId(1L);
        user.setName("João");
        user.setPhotoUrl("/images/default.png");
        user.setInterests("Música");
        user.setRole(Role.STUDENT);
    }

    @Test
    void testGetUserProfileFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponseDTO dto = userService.getUserProfile(1L);

        assertEquals(1L, dto.getId());
        assertEquals("João", dto.getName());
    }

    @Test
    void testGetUserProfileNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.getUserProfile(1L));
    }

    @Test
    void testUpdateProfileWithNameAndInterestsAndFile() {
        when(authService.getAuthenticatedUser()).thenReturn(user);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setName("Maria");
        dto.setInterests("Leitura");

        MockMultipartFile file = new MockMultipartFile("file", "foto.png", "image/png", "fake".getBytes());

        UserResponseDTO dtoResponse = userService.updateProfile(dto, file);

        assertEquals("Maria", dtoResponse.getName());
        assertEquals("Leitura", dtoResponse.getInterests());
        assertTrue(dtoResponse.getPhotoUrl().startsWith("/uploads/"));
    }

    @Test
    void testUpdateProfileWithoutNameAndWithoutFile() {
        when(authService.getAuthenticatedUser()).thenReturn(user);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setName("   "); // blank deve ser ignorado
        dto.setInterests(null); // null deve ser ignorado

        UserResponseDTO dtoResponse = userService.updateProfile(dto, null);

        assertEquals("João", dtoResponse.getName()); // não alterou
        assertEquals("Música", dtoResponse.getInterests()); // não alterou
        assertEquals("/images/default.png", dtoResponse.getPhotoUrl()); // não alterou
    }

    @Test
    void testUpdateProfileWithEmptyFile() {
        when(authService.getAuthenticatedUser()).thenReturn(user);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserUpdateDTO dto = new UserUpdateDTO();

        MockMultipartFile file = new MockMultipartFile("file", "empty.png", "image/png", new byte[0]);

        UserResponseDTO dtoResponse = userService.updateProfile(dto, file);

        assertEquals("/images/default.png", dtoResponse.getPhotoUrl()); // não alterou
    }

    @Test
    void testUpdateProfileWhenStoreFileThrowsException() throws IOException {
        when(authService.getAuthenticatedUser()).thenReturn(user);

        MultipartFile badFile = mock(MultipartFile.class);
        when(badFile.getOriginalFilename()).thenReturn("fail.png");
        when(badFile.isEmpty()).thenReturn(false); // garante que vai tentar salvar
        when(badFile.getInputStream()).thenThrow(new IOException("disk error"));

        UserUpdateDTO dto = new UserUpdateDTO();

        assertThrows(FileStorageException.class, () -> userService.updateProfile(dto, badFile));
    }
}
