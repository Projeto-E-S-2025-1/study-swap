package com.studyswap.api;


import com.studyswap.backend.controller.MaterialController;
import com.studyswap.backend.dto.MaterialResponseDTO;
import com.studyswap.backend.model.ConservationStatus;
import com.studyswap.backend.model.MaterialType;
import com.studyswap.backend.model.TransactionType;
import com.studyswap.backend.service.MaterialService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
public class MaterialControllerGetByIdTest {

    @Mock
    private MaterialService materialService;

    @InjectMocks
    private MaterialController materialController;

    private MaterialResponseDTO testMaterialDTO;

    @BeforeEach
    void setUp() {
        // Prepara a resposta que o service irá retornar em caso de sucesso
        testMaterialDTO = new MaterialResponseDTO(
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
    }

    @Test
    void testGetMaterialById_Success() {
        // Define o comportamento do stub para retornar um DTO quando o ID for 1L
        when(materialService.getMaterialById(1L)).thenReturn(testMaterialDTO);

        // 1. Executa o método que queremos testar
        MaterialResponseDTO response = materialController.getMaterialById(1L);

        // 2. Verifica se o resultado está correto
        assertNotNull(response);
        assertEquals(testMaterialDTO.getId(), response.getId());
        assertEquals(testMaterialDTO.getTitle(), response.getTitle());
    }

    @Test
    void testGetMaterialById_NotFound() {
        // Define o comportamento do stub para lançar uma exceção quando o ID não for encontrado
        when(materialService.getMaterialById(99L))
            .thenThrow(new ResponseStatusException(NOT_FOUND, "Material não encontrado"));

        // 1. Executa e verifica se a exceção esperada é lançada
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            materialController.getMaterialById(99L);
        });

        // 2. Verifica se a exceção tem o status e a mensagem corretos
        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertEquals("404 NOT_FOUND \"Material não encontrado\"", exception.getMessage());
    }
}