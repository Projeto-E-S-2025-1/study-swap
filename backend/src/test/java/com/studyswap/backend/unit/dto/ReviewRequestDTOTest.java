package com.studyswap.backend.unit.dto;

import com.studyswap.backend.dto.ReviewRequestDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

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
        ReviewRequestDTO dto = new ReviewRequestDTO(1L, 2L, 5, "Ótima transação");
        Set<ConstraintViolation<ReviewRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Não deveria haver violações para um DTO válido");
    }

    static Stream<Arguments> invalidDTOs() {
        return Stream.of(
                Arguments.of(new ReviewRequestDTO(1L, 3L, 0, "Comentário válido"), "A nota mínima é 1"),
                Arguments.of(new ReviewRequestDTO(1L, 4L, 6, "Comentário válido"), "A nota máxima é 5"),
                Arguments.of(new ReviewRequestDTO(1L, 5L, 3, "   "), "O comentário é obrigatório"),
                Arguments.of(new ReviewRequestDTO(1L, null, 4, "Comentário válido"), "Transação não identificado")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidDTOs")
    void testInvalidDTOs(ReviewRequestDTO dto, String expectedMessage) {
        Set<ConstraintViolation<ReviewRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty(), "Deveria haver violações para o DTO inválido");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains(expectedMessage)));
    }

    @Test
    void testSettersAndGetters() {
        ReviewRequestDTO dto = new ReviewRequestDTO();
        dto.setTransactionId(123L);
        dto.setRating(4);
        dto.setDescription("Teste");

        assertEquals(123L, dto.getTransactionId());
        assertEquals(4, dto.getRating());
        assertEquals("Teste", dto.getDescription());
    }
}
