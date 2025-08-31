package com.studyswap.backend.unit.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.studyswap.backend.dto.UserReviewAverageDTO;

class UserReviewAverageDTOTest {

    @Test
    void testConstructorAndGetters() {
        Long userId = 1L;
        Double averageRating = 4.5;
        Long totalReviews = 10L;

        UserReviewAverageDTO dto = new UserReviewAverageDTO(userId, averageRating, totalReviews);

        assertEquals(userId, dto.getUserId());
        assertEquals(averageRating, dto.getAverageRating());
        assertEquals(totalReviews, dto.getTotalReviews());
    }

    @Test
    void testNullValues() {
        UserReviewAverageDTO dto = new UserReviewAverageDTO(null, null, null);

        assertNull(dto.getUserId());
        assertNull(dto.getAverageRating());
        assertNull(dto.getTotalReviews());
    }
}
