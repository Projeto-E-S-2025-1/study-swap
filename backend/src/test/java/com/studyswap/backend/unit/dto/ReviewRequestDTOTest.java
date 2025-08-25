package com.studyswap.backend.unit.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.studyswap.backend.dto.ReviewRequestDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ReviewRequestDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidReviewRequestDTO() {
        ReviewRequestDTO dto = new ReviewRequestDTO(1L, 5, "Ótima transação");
        Set<ConstraintViolation<ReviewRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Não deveria haver violações para um DTO válido");
    }

    @Test
    void testInvalidRatingTooLow() {
        ReviewRequestDTO dto = new ReviewRequestDTO(1L, 0, "Comentário válido");
        Set<ConstraintViolation<ReviewRequestDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("A nota mínima é 1")));
    }

    @Test
    void testInvalidRatingTooHigh() {
        ReviewRequestDTO dto = new ReviewRequestDTO(1L, 6, "Comentário válido");
        Set<ConstraintViolation<ReviewRequestDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("A nota máxima é 5")));
    }

    @Test
    void testBlankDescription() {
        ReviewRequestDTO dto = new ReviewRequestDTO(1L, 3, "   ");
        Set<ConstraintViolation<ReviewRequestDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("O comentário é obrigatório")));
    }

    @Test
    void testNullTransactionId() {
        ReviewRequestDTO dto = new ReviewRequestDTO(null, 4, "Comentário válido");
        Set<ConstraintViolation<ReviewRequestDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("A transação avaliada é obrigatória")));
    }
}
