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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;


@ExtendWith(MockitoExtension.class)
public class MaterialControllerGetTest {

    @Mock
    private MaterialService materialService;

    @InjectMocks
    private MaterialController materialController;

    private List<MaterialResponseDTO> allMaterialsList;
    private List<MaterialResponseDTO> searchedMaterialsList;

    @BeforeEach
    void setUp() {
        // Prepara uma lista de materiais de teste para o endpoint de todos os materiais
        allMaterialsList = List.of(
            new MaterialResponseDTO(1L, "Livro A", "Desc A", MaterialType.LIVRO, ConservationStatus.BOM, TransactionType.VENDA, 50.0, "photoA.jpg", 1L, "User A", true),
            new MaterialResponseDTO(2L, "Equipamento B", "Desc B", MaterialType.EQUIPAMENTO, ConservationStatus.NOVO, TransactionType.TROCA, null, "photoB.jpg", 2L, "User B", true)
        );

        // Prepara uma lista de materiais de teste para o endpoint de busca
        searchedMaterialsList = List.of(
            new MaterialResponseDTO(1L, "Livro A", "Desc A", MaterialType.LIVRO, ConservationStatus.BOM, TransactionType.VENDA, 50.0, "photoA.jpg", 1L, "User A", true)
        );
    }

    @Test
    void testGetAllMaterials_Success() {
        // Define o comportamento do stub: o service retorna a lista completa
        when(materialService.getAllMaterials()).thenReturn(allMaterialsList);

        // 1. Executa o método que queremos testar
        List<MaterialResponseDTO> result = materialController.getAllMaterials();

        // 2. Verifica se o resultado está correto
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(allMaterialsList.get(0).getTitle(), result.get(0).getTitle());
    }

    @Test
    void testSearchMaterials_Success() {
        // Define o comportamento do stub: o service retorna a lista filtrada
        when(materialService.searchMaterials(
            eq("Livro"), // Título de busca
            eq(MaterialType.LIVRO), // Tipo de material
            isNull(), // Conservação (nulo para este teste)
            isNull() // Transação (nulo para este teste)
        )).thenReturn(searchedMaterialsList);

        // 1. Executa o método que queremos testar com os parâmetros de busca
        List<MaterialResponseDTO> result = materialController.searchMaterials(
            "Livro", MaterialType.LIVRO, null, null
        );

        // 2. Verifica se o resultado está correto
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(searchedMaterialsList.get(0).getTitle(), result.get(0).getTitle());
    }
}