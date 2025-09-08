package com.studyswap.backend.unit.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.studyswap.backend.dto.ReviewResponseDTO;

class ReviewResponseDTOTest {

    @Test
    void testGettersAndSetters() {
        ReviewResponseDTO dto = new ReviewResponseDTO();

        Long id = 1L;
        String authorName = "Carlos";
        Long authorId = 10L;
        String materialTitle = "O conto da aia";
        Integer rating = 5;
        String description = "Ótima transação!";
        LocalDateTime createdAt = LocalDateTime.now();

        dto.setId(id);
        dto.setAuthorName(authorName);
        dto.setAuthorId(authorId);
        dto.setMaterialTitle(materialTitle);
        dto.setRating(rating);
        dto.setDescription(description);
        dto.setCreatedAt(createdAt);

        assertEquals(id, dto.getId());
        assertEquals(authorName, dto.getAuthorName());
        assertEquals(authorId, dto.getAuthorId());
        assertEquals(materialTitle, dto.getMaterialTitle());
        assertEquals(rating, dto.getRating());
        assertEquals(description, dto.getDescription());
        assertEquals(createdAt, dto.getCreatedAt());
    }

    @Test
    void testDefaultValues() {
        ReviewResponseDTO dto = new ReviewResponseDTO();

        assertNull(dto.getId());
        assertNull(dto.getAuthorName());
        assertNull(dto.getAuthorId());
        assertNull(dto.getMaterialTitle());
        assertNull(dto.getRating());
        assertNull(dto.getDescription());
        assertNull(dto.getCreatedAt());
    }
}
