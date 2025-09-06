package com.studyswap.backend.unit.dto;

import com.studyswap.backend.dto.MaterialDTO;
import com.studyswap.backend.model.ConservationStatus;
import com.studyswap.backend.model.MaterialType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MaterialDTOTest {

    @Test
    void testConstructorAndGetters() {
        MaterialDTO dto = new MaterialDTO(
                1L,
                "Livro de POO",
                MaterialType.LIVRO,
                ConservationStatus.NOVO
        );

        assertEquals(1L, dto.getId());
        assertEquals("Livro de POO", dto.getTitle());
        assertEquals(MaterialType.LIVRO, dto.getType());
        assertEquals(ConservationStatus.NOVO, dto.getConservationStatus());
    }

    @Test
    void testSetters() {
        MaterialDTO dto = new MaterialDTO(
                null,
                null,
                null,
                null
        );

        dto.setId(2L);
        dto.setTitle("Estruturas de Dados");
        dto.setType(MaterialType.APOSTILA);
        dto.setConservationStatus(ConservationStatus.RAZOAVEL);

        assertEquals(2L, dto.getId());
        assertEquals("Estruturas de Dados", dto.getTitle());
        assertEquals(MaterialType.APOSTILA, dto.getType());
        assertEquals(ConservationStatus.RAZOAVEL, dto.getConservationStatus());
    }
}
