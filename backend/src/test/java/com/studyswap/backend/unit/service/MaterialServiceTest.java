package com.studyswap.backend.unit.service;

import com.studyswap.backend.dto.MaterialRequestDTO;
import com.studyswap.backend.dto.MaterialResponseDTO;
import com.studyswap.backend.model.*;
import com.studyswap.backend.repository.MaterialRepository;
import com.studyswap.backend.service.MaterialService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MaterialServiceTest {

    @Mock
    private MaterialRepository materialRepository;

    @InjectMocks
    private MaterialService materialService;

    private User testUser;
    private User anotherUser;
    private Material materialEntity;
    private MaterialRequestDTO createRequestDTO;
    private MaterialRequestDTO updateRequestDTO;
    private MultipartFile testFile;

    @BeforeEach
    void setUp() {
        // Usuários
        testUser = new User("Test User", "test@example.com", "password", null);
        testUser.setId(1L);

        anotherUser = new User("Another User", "another@example.com", "password", null);
        anotherUser.setId(2L);

        // DTOs
        createRequestDTO = new MaterialRequestDTO(
                "Livro de Teste", "Descrição do livro", MaterialType.LIVRO,
                ConservationStatus.NOVO, TransactionType.VENDA, 50.0, true
        );

        updateRequestDTO = new MaterialRequestDTO(
                "Livro Atualizado", "Descrição atualizada", MaterialType.LIVRO,
                ConservationStatus.BOM, TransactionType.VENDA, 45.0, true
        );

        // Arquivo
        testFile = new MockMultipartFile(
                "file", "test_image.jpg", "image/jpeg",
                "conteúdo do arquivo".getBytes()
        );

        // Entidade Material
        materialEntity = new Material();
        materialEntity.setId(1L);
        materialEntity.setTitle("Livro de Teste");
        materialEntity.setDescription("Descrição do livro");
        materialEntity.setUser(testUser);
        materialEntity.setPhoto("/uploads/old_image.jpg");
    }

    // ---------------------- createMaterial ----------------------

    @Test
    void testCreateMaterial_WithFile_Success() {
        // O método storeFile() é privado e usa classes estáticas, focaremos em verificar
        // se o repositório é chamado corretamente e se a foto não é a padrão.
        when(materialRepository.save(any(Material.class))).thenAnswer(invocation -> {
            Material saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        MaterialResponseDTO result = materialService.createMaterial(createRequestDTO, testUser, testFile);

        assertNotNull(result);
        assertEquals(createRequestDTO.getTitle(), result.getTitle());
        assertEquals(testUser.getId(), result.getUserId());
        assertNotNull(result.getPhoto());
        assertNotEquals("/images/default-photo.png", result.getPhoto()); // Verifica que não usou a foto padrão
        verify(materialRepository, times(1)).save(any(Material.class));
    }

    @Test
    void testCreateMaterial_WithoutFile_Success() {
        when(materialRepository.save(any(Material.class))).thenAnswer(invocation -> {
            Material saved = invocation.getArgument(0);
            saved.setId(1L);
            saved.setPhoto("/images/default-photo.png"); // Simula o comportamento do serviço
            return saved;
        });

        MaterialResponseDTO result = materialService.createMaterial(createRequestDTO, testUser, null);

        assertNotNull(result);
        assertEquals(createRequestDTO.getTitle(), result.getTitle());
        assertEquals("/images/default-photo.png", result.getPhoto()); // Verifica que usou a foto padrão
        verify(materialRepository, times(1)).save(any(Material.class));
    }

    @Test
    void testCreateMaterial_WithEmptyFile_ShouldUseDefaultPhoto() {
        MultipartFile emptyFile = new MockMultipartFile("file", "empty.jpg", "image/jpeg", new byte[0]);

        when(materialRepository.save(any(Material.class))).thenAnswer(invocation -> {
            Material saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        MaterialResponseDTO result = materialService.createMaterial(createRequestDTO, testUser, emptyFile);

        assertNotNull(result);
        assertEquals("/images/default-photo.png", result.getPhoto()); // Deve usar a foto padrão
        verify(materialRepository, times(1)).save(any(Material.class));
    }

    // ---------------------- updateMaterial ----------------------

    @Test
    void testUpdateMaterial_Success() {
        when(materialRepository.findById(1L)).thenReturn(Optional.of(materialEntity));
        when(materialRepository.save(any(Material.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MaterialResponseDTO result = materialService.updateMaterial(1L, updateRequestDTO, testUser, null);

        assertNotNull(result);
        assertEquals(updateRequestDTO.getTitle(), result.getTitle());
        assertEquals(updateRequestDTO.getPrice(), result.getPrice());
        assertEquals(materialEntity.getPhoto(), result.getPhoto()); // Foto não deve mudar se o arquivo for nulo
        verify(materialRepository, times(1)).findById(1L);
        verify(materialRepository, times(1)).save(any(Material.class));
    }

    @Test
    void testUpdateMaterial_NotFound() {
        when(materialRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            materialService.updateMaterial(99L, updateRequestDTO, testUser, null);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void testUpdateMaterial_Forbidden() {
        when(materialRepository.findById(1L)).thenReturn(Optional.of(materialEntity));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            materialService.updateMaterial(1L, updateRequestDTO, anotherUser, null);
        });

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("Você não tem permissão para editar este material"));
    }

    @Test
    void testUpdateMaterial_WithFile_ShouldUpdatePhoto() {
        when(materialRepository.findById(1L)).thenReturn(Optional.of(materialEntity));
        when(materialRepository.save(any(Material.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Salva a foto antiga antes de atualizar
        String oldPhoto = materialEntity.getPhoto();

        MaterialResponseDTO result = materialService.updateMaterial(1L, updateRequestDTO, testUser, testFile);

        assertNotNull(result);
        assertEquals(updateRequestDTO.getTitle(), result.getTitle());
        assertEquals(testUser.getId(), result.getUserId());
        assertNotNull(result.getPhoto());
        assertNotEquals(oldPhoto, result.getPhoto()); // compara com o valor antigo
        verify(materialRepository, times(1)).findById(1L);
        verify(materialRepository, times(1)).save(any(Material.class));
    }

    @Test
    void testUpdateMaterial_WithEmptyFile_ShouldNotChangePhoto() {
        MultipartFile emptyFile = new MockMultipartFile("file", "empty.jpg", "image/jpeg", new byte[0]);

        when(materialRepository.findById(1L)).thenReturn(Optional.of(materialEntity));
        when(materialRepository.save(any(Material.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String oldPhoto = materialEntity.getPhoto();

        MaterialResponseDTO result = materialService.updateMaterial(1L, updateRequestDTO, testUser, emptyFile);

        assertNotNull(result);
        assertEquals(oldPhoto, result.getPhoto()); // Foto não deve mudar
        verify(materialRepository, times(1)).findById(1L);
        verify(materialRepository, times(1)).save(any(Material.class));
    }

    // ---------------------- getMaterialById ----------------------

    @Test
    void testGetMaterialById_Success() {
        when(materialRepository.findById(1L)).thenReturn(Optional.of(materialEntity));

        MaterialResponseDTO result = materialService.getMaterialById(1L);

        assertNotNull(result);
        assertEquals(materialEntity.getId(), result.getId());
        assertEquals(materialEntity.getTitle(), result.getTitle());
    }

    @Test
    void testGetMaterialById_NotFound() {
        when(materialRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            materialService.getMaterialById(99L);
        });
    }

    // ---------------------- getAllMaterials / searchMaterials ----------------------

    @Test
    void testGetAllMaterials_Success() {
        Material anotherMaterial = new Material();
        anotherMaterial.setId(2L);
        anotherMaterial.setTitle("Outro Material de Teste");
        anotherMaterial.setUser(anotherUser); 

        List<Material> materials = List.of(materialEntity, anotherMaterial);

        when(materialRepository.findAll()).thenReturn(materials);

        List<MaterialResponseDTO> result = materialService.getAllMaterials();

        assertEquals(2, result.size());
        verify(materialRepository, times(1)).findAll();
    }

    @Test
    void testSearchMaterials_Success() {
        List<Material> materials = List.of(materialEntity);
        when(materialRepository.searchByFilters(any(), any(), any(), any())).thenReturn(materials);

        List<MaterialResponseDTO> result = materialService.searchMaterials("Livro", MaterialType.LIVRO, null, null);

        assertEquals(1, result.size());
        assertEquals("Livro de Teste", result.get(0).getTitle());
        verify(materialRepository, times(1)).searchByFilters("Livro", MaterialType.LIVRO, null, null);
    }

    // ---------------------- deleteMaterial ----------------------

    @Test
    void testDeleteMaterial_Success() {
        when(materialRepository.findById(1L)).thenReturn(Optional.of(materialEntity));
        doNothing().when(materialRepository).delete(materialEntity);

        materialService.deleteMaterial(1L, testUser);

        verify(materialRepository, times(1)).findById(1L);
        verify(materialRepository, times(1)).delete(materialEntity);
    }

    @Test
    void testDeleteMaterial_NotFound() {
        when(materialRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            materialService.deleteMaterial(99L, testUser);
        });
    }

    @Test
    void testDeleteMaterial_Forbidden() {
        when(materialRepository.findById(1L)).thenReturn(Optional.of(materialEntity));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            materialService.deleteMaterial(1L, anotherUser);
        });

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("Você não tem permissão para remover este material"));
        verify(materialRepository, never()).delete(any());
    }
}