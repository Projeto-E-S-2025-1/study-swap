package com.studyswap.backend.unit.dto;

import com.studyswap.backend.dto.MaterialResponseDTO;
import com.studyswap.backend.model.ConservationStatus;
import com.studyswap.backend.model.MaterialType;
import com.studyswap.backend.model.TransactionType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MaterialResponseDTOTest {

    @Test
    void testConstructorWithAllArgs() {
        MaterialResponseDTO dto = new MaterialResponseDTO(
                1L,
                "Título",
                "Descrição",
                MaterialType.LIVRO,
                ConservationStatus.NOVO,
                TransactionType.VENDA,
                99.9,
                "foto.png",
                10L,
                "Usuário Teste",
                true
        );

        assertEquals(1L, dto.getId());
        assertEquals("Título", dto.getTitle());
        assertEquals("Descrição", dto.getDescription());
        assertEquals(MaterialType.LIVRO, dto.getMaterialType());
        assertEquals(ConservationStatus.NOVO, dto.getConservationStatus());
        assertEquals(TransactionType.VENDA, dto.getTransactionType());
        assertEquals(99.9, dto.getPrice());
        assertEquals("foto.png", dto.getPhoto());
        assertEquals(10L, dto.getUserId());
        assertEquals("Usuário Teste", dto.getUserName());
        assertTrue(dto.isAvailable());
    }

    @Test
    void testSettersAndGetters() {
        MaterialResponseDTO dto = new MaterialResponseDTO();

        dto.setId(2L);
        dto.setTitle("Novo título");
        dto.setDescription("Nova descrição");
        dto.setMaterialType(MaterialType.APOSTILA);
        dto.setConservationStatus(ConservationStatus.BOM);
        dto.setTransactionType(TransactionType.DOACAO);
        dto.setPrice(0.0);
        dto.setPhoto("imagem.jpg");
        dto.setUserId(20L);
        dto.setUserName("Outro Usuário");
        dto.setAvailable(false);

        assertEquals(2L, dto.getId());
        assertEquals("Novo título", dto.getTitle());
        assertEquals("Nova descrição", dto.getDescription());
        assertEquals(MaterialType.APOSTILA, dto.getMaterialType());
        assertEquals(ConservationStatus.BOM, dto.getConservationStatus());
        assertEquals(TransactionType.DOACAO, dto.getTransactionType());
        assertEquals(0.0, dto.getPrice());
        assertEquals("imagem.jpg", dto.getPhoto());
        assertEquals(20L, dto.getUserId());
        assertEquals("Outro Usuário", dto.getUserName());
        assertFalse(dto.isAvailable());
    }

    @Test
    void testEqualsAndHashCode() {
        MaterialResponseDTO dto1 = new MaterialResponseDTO();
        dto1.setId(1L);

        MaterialResponseDTO dto2 = new MaterialResponseDTO();
        dto2.setId(1L);

        MaterialResponseDTO dto3 = new MaterialResponseDTO();
        dto3.setId(2L);

        Object notADTO = new Object();

        // equals true
        assertEquals(dto1, dto2);

        // equals false com id diferente
        assertNotEquals(dto1, dto3);

        // equals false com null
        assertNotEquals(null, dto1);

        // equals false com classe diferente
        assertNotEquals(dto1, notADTO);

        // hashCode
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testEqualsWithNullId() {
        MaterialResponseDTO dto1 = new MaterialResponseDTO();
        dto1.setId(null);

        MaterialResponseDTO dto2 = new MaterialResponseDTO();
        dto2.setId(1L);

        MaterialResponseDTO dto3 = new MaterialResponseDTO();
        dto3.setId(null);

        // dto1 vs dto2 -> ids diferentes, um é null
        assertNotEquals(dto1, dto2);

        // dto1 vs dto3 -> ambos null -> deve ser true
        assertEquals(dto1, dto3);
    }

    @Test
    void testEqualsSameInstance() {
        MaterialResponseDTO dto = new MaterialResponseDTO();
        dto.setId(1L);

        // compara o objeto com ele mesmo
        assertEquals(dto, dto);
    }
}
