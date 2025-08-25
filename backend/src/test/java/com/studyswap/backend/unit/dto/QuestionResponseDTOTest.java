package com.studyswap.backend.unit.dto;

import org.junit.jupiter.api.Test;

import com.studyswap.backend.dto.QuestionResponseDTO;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class QuestionResponseDTOTest {

    @Test
    void testConstructorAndGetters() {
        LocalDateTime now = LocalDateTime.now();

        QuestionResponseDTO dto = new QuestionResponseDTO(
                1L,
                "Título da questão",
                "Descrição da questão",
                "Autor Teste",
                100L,
                now
        );

        assertEquals(1L, dto.getId());
        assertEquals("Título da questão", dto.getTitle());
        assertEquals("Descrição da questão", dto.getDescription());
        assertEquals("Autor Teste", dto.getAuthorName());
        assertEquals(100L, dto.getAuthorId());
        assertEquals(now, dto.getCreatedAt());
    }

    @Test
    void testSetters() {
        LocalDateTime createdAt = LocalDateTime.now();
        QuestionResponseDTO dto = new QuestionResponseDTO(
                null, null, null, null, null, null
        );

        dto.setId(2L);
        dto.setTitle("Novo título");
        dto.setDescription("Nova descrição");
        dto.setAuthorName("Novo autor");
        dto.setAuthorId(200L);
        dto.setCreatedAt(createdAt);

        assertEquals(2L, dto.getId());
        assertEquals("Novo título", dto.getTitle());
        assertEquals("Nova descrição", dto.getDescription());
        assertEquals("Novo autor", dto.getAuthorName());
        assertEquals(200L, dto.getAuthorId());
        assertEquals(createdAt, dto.getCreatedAt());
    }
}
