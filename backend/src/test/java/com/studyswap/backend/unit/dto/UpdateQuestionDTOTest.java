package com.studyswap.backend.unit.dto;

import com.studyswap.backend.dto.UpdateQuestionDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdateQuestionDTOTest {

    @Test
    void testSettersAndGetters() {
        UpdateQuestionDTO dto = new UpdateQuestionDTO();

        dto.setTitle("Novo título");
        dto.setDescription("Nova descrição");

        assertEquals("Novo título", dto.getTitle());
        assertEquals("Nova descrição", dto.getDescription());
    }

    @Test
    void testDefaultValues() {
        UpdateQuestionDTO dto = new UpdateQuestionDTO();

        assertNull(dto.getTitle());
        assertNull(dto.getDescription());
    }
}
