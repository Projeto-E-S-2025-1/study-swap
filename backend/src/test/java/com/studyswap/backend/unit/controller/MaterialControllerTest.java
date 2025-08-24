package com.studyswap.backend.unit.controller;

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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MaterialControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private MaterialService materialService;

    @InjectMocks
    private MaterialController materialController;

    private User testUser;
    private User anotherUser;

    private MaterialRequestDTO materialRequestDTO;
    private MaterialResponseDTO materialResponseDTO;
    private MultipartFile testFile;

    private MaterialRequestDTO updatedMaterialDTO;
    private MaterialResponseDTO updatedResponseDTO;
    private MultipartFile updatedFile;

    private List<MaterialResponseDTO> allMaterialsList;
    private List<MaterialResponseDTO> searchedMaterialsList;

    @BeforeEach
    void setUp() {
        // Usuários
        testUser = new User("Test User", "test@example.com", "password", null);
        testUser.setId(1L);

        anotherUser = new User("Another User", "another@example.com", "password", null);
        anotherUser.setId(2L);

        // DTOs de criação
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

        testFile = new MockMultipartFile(
            "file",
            "test_image.jpg",
            "image/jpeg",
            "conteúdo do arquivo".getBytes()
        );

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

        // DTOs de atualização
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

        updatedFile = new MockMultipartFile(
            "file",
            "updated_image.jpg",
            "image/jpeg",
            "conteúdo do arquivo atualizado".getBytes()
        );

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

        // Listas para testes de busca/listagem
        allMaterialsList = List.of(
            new MaterialResponseDTO(1L, "Livro A", "Desc A", MaterialType.LIVRO, ConservationStatus.BOM, TransactionType.VENDA, 50.0, "photoA.jpg", 1L, "User A", true),
            new MaterialResponseDTO(2L, "Equipamento B", "Desc B", MaterialType.EQUIPAMENTO, ConservationStatus.NOVO, TransactionType.TROCA, null, "photoB.jpg", 2L, "User B", true)
        );

        searchedMaterialsList = List.of(
            new MaterialResponseDTO(1L, "Livro A", "Desc A", MaterialType.LIVRO, ConservationStatus.BOM, TransactionType.VENDA, 50.0, "photoA.jpg", 1L, "User A", true)
        );
    }

    // ---------------------- CREATE ----------------------

    @Test
    void testCreateMaterial_Success() {
        when(authService.getAuthenticatedUser()).thenReturn(testUser);
        when(materialService.createMaterial(any(MaterialRequestDTO.class), any(User.class), any(MultipartFile.class)))
            .thenReturn(materialResponseDTO);

        ResponseEntity<MaterialResponseDTO> response = materialController.createMaterial(materialRequestDTO, testFile);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(materialResponseDTO.getTitle(), response.getBody().getTitle());
        assertEquals(materialResponseDTO.getUserId(), response.getBody().getUserId());
    }

    // ---------------------- DELETE ----------------------

    @Test
    void testDeleteMaterial_Success() {
        when(authService.getAuthenticatedUser()).thenReturn(testUser);

        ResponseEntity<String> response = materialController.deleteMaterial(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Material deletado com sucesso!", response.getBody());
        verify(materialService, times(1)).deleteMaterial(1L, testUser);
    }

    @Test
    void testDeleteMaterial_NotFound() {
        when(authService.getAuthenticatedUser()).thenReturn(testUser);
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Material não encontrado"))
            .when(materialService).deleteMaterial(eq(99L), any(User.class));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            materialController.deleteMaterial(99L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("404 NOT_FOUND \"Material não encontrado\"", exception.getMessage());
    }

    @Test
    void testDeleteMaterial_Forbidden() {
        when(authService.getAuthenticatedUser()).thenReturn(anotherUser);
        doThrow(new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para remover este material"))
            .when(materialService).deleteMaterial(eq(1L), any(User.class));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            materialController.deleteMaterial(1L);
        });

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("403 FORBIDDEN \"Você não tem permissão para remover este material\"", exception.getMessage());
    }

    // ---------------------- GET BY ID ----------------------

    @Test
    void testGetMaterialById_Success() {
        when(materialService.getMaterialById(1L)).thenReturn(materialResponseDTO);

        MaterialResponseDTO response = materialController.getMaterialById(1L);

        assertNotNull(response);
        assertEquals(materialResponseDTO.getId(), response.getId());
        assertEquals(materialResponseDTO.getTitle(), response.getTitle());
    }

    @Test
    void testGetMaterialById_NotFound() {
        when(materialService.getMaterialById(99L))
            .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Material não encontrado"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            materialController.getMaterialById(99L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("404 NOT_FOUND \"Material não encontrado\"", exception.getMessage());
    }

    // ---------------------- GET ALL / SEARCH ----------------------

    @Test
    void testGetAllMaterials_Success() {
        when(materialService.getAllMaterials()).thenReturn(allMaterialsList);

        List<MaterialResponseDTO> result = materialController.getAllMaterials();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(allMaterialsList.get(0).getTitle(), result.get(0).getTitle());
    }

    @Test
    void testSearchMaterials_Success() {
        when(materialService.searchMaterials(eq("Livro"), eq(MaterialType.LIVRO), isNull(), isNull()))
            .thenReturn(searchedMaterialsList);

        List<MaterialResponseDTO> result = materialController.searchMaterials("Livro", MaterialType.LIVRO, null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(searchedMaterialsList.get(0).getTitle(), result.get(0).getTitle());
    }

    // ---------------------- UPDATE ----------------------

    @Test
    void testUpdateMaterial_Success() {
        when(authService.getAuthenticatedUser()).thenReturn(testUser);
        when(materialService.updateMaterial(eq(1L), any(MaterialRequestDTO.class), any(User.class), any(MultipartFile.class)))
            .thenReturn(updatedResponseDTO);

        ResponseEntity<MaterialResponseDTO> response = materialController.updateMaterial(1L, updatedMaterialDTO, updatedFile);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Livro de Teste Atualizado", response.getBody().getTitle());
    }

    @Test
    void testUpdateMaterial_NotFound() {
        when(authService.getAuthenticatedUser()).thenReturn(testUser);
        when(materialService.updateMaterial(eq(99L), any(MaterialRequestDTO.class), any(User.class), any(MultipartFile.class)))
            .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Material não encontrado"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            materialController.updateMaterial(99L, updatedMaterialDTO, updatedFile);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("404 NOT_FOUND \"Material não encontrado\"", exception.getMessage());
    }

    @Test
    void testUpdateMaterial_Forbidden() {
        when(authService.getAuthenticatedUser()).thenReturn(anotherUser);
        when(materialService.updateMaterial(eq(1L), any(MaterialRequestDTO.class), any(User.class), any(MultipartFile.class)))
            .thenThrow(new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para editar este material"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            materialController.updateMaterial(1L, updatedMaterialDTO, updatedFile);
        });

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("403 FORBIDDEN \"Você não tem permissão para editar este material\"", exception.getMessage());
    }
}
