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
        Integer rating = 5;
        Long userId = 10L;
        String description = "Ótima transação!";
        LocalDateTime createdAt = LocalDateTime.now();

        dto.setId(id);
        dto.setRating(rating);
        dto.setUserId(userId);
        dto.setDescription(description);
        dto.setCreatedAt(createdAt);

        assertEquals(id, dto.getId());
        assertEquals(rating, dto.getRating());
        assertEquals(userId, dto.getUserId());
        assertEquals(description, dto.getDescription());
        assertEquals(createdAt, dto.getCreatedAt());
    }

    @Test
    void testDefaultValues() {
        ReviewResponseDTO dto = new ReviewResponseDTO();

        assertNull(dto.getId());
        assertNull(dto.getRating());
        assertNull(dto.getUserId());
        assertNull(dto.getDescription());
        assertNull(dto.getCreatedAt());
    }
}
