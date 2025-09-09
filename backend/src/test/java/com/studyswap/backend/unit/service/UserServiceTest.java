    package com.studyswap.backend.unit.service;
    
    import static org.junit.jupiter.api.Assertions.*;
    import static org.mockito.ArgumentMatchers.any;
    import static org.mockito.Mockito.mock;
    import static org.mockito.Mockito.when;
    
    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Optional;

    import com.studyswap.backend.dto.MaterialDTO;
    import com.studyswap.backend.dto.MaterialResponseDTO;
    import com.studyswap.backend.model.Material;
    import jakarta.validation.constraints.AssertFalse;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;
    import org.springframework.mock.web.MockMultipartFile;
    import org.springframework.web.multipart.MultipartFile;
    import org.springframework.web.server.ResponseStatusException;
    
    import com.studyswap.backend.dto.UserResponseDTO;
    import com.studyswap.backend.dto.UserUpdateDTO;
    import com.studyswap.backend.model.Role;
    import com.studyswap.backend.model.User;
    import com.studyswap.backend.repository.MaterialRepository;
    import com.studyswap.backend.repository.UserRepository;
    import com.studyswap.backend.service.AuthService;
    import com.studyswap.backend.service.UserService;
    import com.studyswap.backend.service.exception.FileStorageException;
    
    class UserServiceTest {
    
        private UserRepository userRepository;
        private AuthService authService;
        private UserService userService;
        private MaterialRepository materialRepository;
        private User user;
    
        @BeforeEach
        void setUp() {
            userRepository = mock(UserRepository.class);
            authService = mock(AuthService.class);
            materialRepository=mock(MaterialRepository.class);
            
            userService = new UserService(userRepository, authService, materialRepository);
    
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
    @Test
    void testFavoriteExistentMaterial() throws ResponseStatusException{
        when(authService.getAuthenticatedUser()).thenReturn(user);

        Material material = new Material();
        material.setId(1L);
        material.setTitle("O Pequeno principe");

        when(materialRepository.findById(1L)).thenReturn(Optional.of(material));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        userService.favoriteMaterial(1L);
        assertTrue(user.getFavoriteMaterials().contains(material));
    }
    @Test
    void testFavoriteNotExistentMaterial() throws ResponseStatusException{
        when(authService.getAuthenticatedUser()).thenReturn(user);

        when(materialRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> {
            userService.favoriteMaterial(1L);
        });
    }
    @Test
    void testFavoriteAlreadyFavoriteMaterial() throws ResponseStatusException{
        when(authService.getAuthenticatedUser()).thenReturn(user);
        Material material = new Material();
        material.setId(1L);
        material.setTitle("O Pequeno principe");
        when(materialRepository.findById(1L)).thenReturn(Optional.of(material));
        user.getFavoriteMaterials().add(material);
        assertThrows(ResponseStatusException.class, ()->{userService.favoriteMaterial(1L);
        });
    }
    

}
