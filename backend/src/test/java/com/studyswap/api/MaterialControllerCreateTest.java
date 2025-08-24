package com.studyswap.api;

import com.studyswap.backend.controller.MaterialController;
import com.studyswap.backend.dto.MaterialRequestDTO;
import com.studyswap.backend.dto.MaterialResponseDTO;
import com.studyswap.backend.model.ConservationStatus;
import com.studyswap.backend.model.MaterialType;
import com.studyswap.backend.model.TransactionType;
import com.studyswap.backend.model.User;
import com.studyswap.backend.service.AuthService;
import com.studyswap.backend.service.MaterialService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MaterialControllerCreateTest {

    @Mock
    private AuthService authService;

    @Mock
    private MaterialService materialService;

    @InjectMocks
    private MaterialController materialController;

    private User testUser;
    private MaterialRequestDTO materialRequestDTO;
    private MultipartFile testFile;
    private MaterialResponseDTO materialResponseDTO;

    @BeforeEach
    void setUp() {
        // Prepara um usuário de teste
        testUser = new User("Test User", "test@example.com", "password", null);
        testUser.setId(1L);

        // Prepara os dados de entrada
        materialRequestDTO = new MaterialRequestDTO(
            "Livro de Teste", 
            "Descrição do livro",
            MaterialType.LIVRO, 
            ConservationStatus.NOVO,
            TransactionType.VENDA, 
            50.0, 
            null,
            true
        );

        // Prepara um arquivo de teste simulado
        testFile = new MockMultipartFile(
            "file", 
            "test_image.jpg", 
            "image/jpeg", 
            "conteúdo do arquivo".getBytes()
        );

        // Prepara a resposta que o stub do service irá retornar
        materialResponseDTO = new MaterialResponseDTO(
            1L,
            "Livro de Teste",
            "Descrição do livro",
            MaterialType.LIVRO,
            ConservationStatus.NOVO,
            TransactionType.VENDA,
            50.0,
            "/uploads/test_image.jpg",
            1L,
            "Test User",
            true
        );

        // Define o comportamento dos stubs
        when(authService.getAuthenticatedUser()).thenReturn(testUser);
        when(materialService.createMaterial(
            any(MaterialRequestDTO.class), 
            any(User.class), 
            any(MultipartFile.class))
        ).thenReturn(materialResponseDTO);
    }

    @Test
    void testCreateMaterial_Success() {
        // 1. Executa o método que queremos testar
        ResponseEntity<MaterialResponseDTO> response = materialController.createMaterial(materialRequestDTO, testFile);

        // 2. Verifica se o resultado está correto
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(materialResponseDTO.getTitle(), response.getBody().getTitle());
        assertEquals(materialResponseDTO.getUserId(), response.getBody().getUserId());
    }
}