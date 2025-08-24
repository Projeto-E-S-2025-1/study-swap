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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MaterialControllerUpdateTest {

    @Mock
    private AuthService authService;

    @Mock
    private MaterialService materialService;

    @InjectMocks
    private MaterialController materialController;

    private User testUser;
    private MaterialRequestDTO updatedMaterialDTO;
    private MultipartFile testFile;
    private MaterialResponseDTO updatedResponseDTO;

    @BeforeEach
    void setUp() {
        // Prepara um usuário de teste que é o dono do material
        testUser = new User("Test User", "test@example.com", "password", null);
        testUser.setId(1L);

        // Prepara os dados de entrada para a atualização
        updatedMaterialDTO = new MaterialRequestDTO(
            "Livro de Teste Atualizado",
            "Descrição do livro atualizada",
            MaterialType.LIVRO,
            ConservationStatus.BOM,
            TransactionType.VENDA,
            45.0,
            "/uploads/test_image.jpg",
            true
        );

        // Prepara um arquivo de teste simulado
        testFile = new MockMultipartFile(
            "file",
            "updated_image.jpg",
            "image/jpeg",
            "conteúdo do arquivo atualizado".getBytes()
        );

        // Prepara a resposta que o service irá retornar em caso de sucesso
        updatedResponseDTO = new MaterialResponseDTO(
            1L,
            "Livro de Teste Atualizado",
            "Descrição do livro atualizada",
            MaterialType.LIVRO,
            ConservationStatus.BOM,
            TransactionType.VENDA,
            45.0,
            "/uploads/updated_image.jpg",
            1L,
            "Test User",
            true
        );
    }

    @Test
    void testUpdateMaterial_Success() {
        // Define o comportamento dos stubs para o cenário de sucesso
        when(authService.getAuthenticatedUser()).thenReturn(testUser);
        when(materialService.updateMaterial(
            eq(1L), // O ID do material deve ser igual a 1L
            any(MaterialRequestDTO.class),
            any(User.class),
            any(MultipartFile.class))
        ).thenReturn(updatedResponseDTO);

        // 1. Executa o método que queremos testar
        ResponseEntity<MaterialResponseDTO> response = materialController.updateMaterial(1L, updatedMaterialDTO, testFile);

        // 2. Verifica se o resultado está correto
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Livro de Teste Atualizado", response.getBody().getTitle());
    }

    @Test
    void testUpdateMaterial_NotFound() {
        // Define o comportamento do stub para simular material não encontrado
        when(authService.getAuthenticatedUser()).thenReturn(testUser);
        when(materialService.updateMaterial(
            eq(99L), // Simula um ID que não existe
            any(MaterialRequestDTO.class),
            any(User.class),
            any(MultipartFile.class))
        ).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Material não encontrado"));

        // 1. Executa e verifica se a exceção esperada é lançada
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            materialController.updateMaterial(99L, updatedMaterialDTO, testFile);
        });

        // 2. Verifica se a exceção tem o status e a mensagem corretos
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("404 NOT_FOUND \"Material não encontrado\"", exception.getMessage());
    }

    @Test
    void testUpdateMaterial_Forbidden() {
        // Prepara um segundo usuário que não é o dono do material
        User anotherUser = new User("Another User", "another@example.com", "password", null);
        anotherUser.setId(2L);
        
        // Define o comportamento do stub para simular falta de permissão
        when(authService.getAuthenticatedUser()).thenReturn(anotherUser);
        when(materialService.updateMaterial(
            eq(1L), // O material é do usuário 1, mas o usuário 2 está tentando editar
            any(MaterialRequestDTO.class),
            any(User.class),
            any(MultipartFile.class))
        ).thenThrow(new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para editar este material"));

        // 1. Executa e verifica se a exceção esperada é lançada
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            materialController.updateMaterial(1L, updatedMaterialDTO, testFile);
        });

        // 2. Verifica se a exceção tem o status e a mensagem corretos
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("403 FORBIDDEN \"Você não tem permissão para editar este material\"", exception.getMessage());
    }
}