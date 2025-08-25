package com.studyswap.backend.unit.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.studyswap.backend.dto.CreateQuestionDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CreateQuestionDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidDTO() {
        CreateQuestionDTO dto = new CreateQuestionDTO("Título válido", "Descrição válida", 1L);

        Set<ConstraintViolation<CreateQuestionDTO>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), "Não deve haver violações para um DTO válido");
    }

    @Test
    void testTitleIsBlank() {
        CreateQuestionDTO dto = new CreateQuestionDTO("", "Descrição válida", 1L);

        Set<ConstraintViolation<CreateQuestionDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("O título é obrigatório")));
    }

    @Test
    void testDescriptionIsBlank() {
        CreateQuestionDTO dto = new CreateQuestionDTO("Título válido", "   ", 1L);

        Set<ConstraintViolation<CreateQuestionDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("A descrição é obrigatória")));
    }

    @Test
    void testMaterialIdIsNull() {
        CreateQuestionDTO dto = new CreateQuestionDTO("Título válido", "Descrição válida", null);

        Set<ConstraintViolation<CreateQuestionDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("O ID do material é obrigatório")));
    }

    @Test
    void testGettersAndSetters() {
        CreateQuestionDTO dto = new CreateQuestionDTO();

        dto.setTitle("Novo Título");
        dto.setDescription("Nova Descrição");
        dto.setMaterialId(10L);

        assertEquals("Novo Título", dto.getTitle());
        assertEquals("Nova Descrição", dto.getDescription());
        assertEquals(10L, dto.getMaterialId());
    }
}
