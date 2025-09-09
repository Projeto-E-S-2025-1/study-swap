package com.studyswap.backend.unit.dto;

import com.studyswap.backend.dto.MaterialRequestDTO;
import com.studyswap.backend.model.ConservationStatus;
import com.studyswap.backend.model.MaterialType;
import com.studyswap.backend.model.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MaterialRequestDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private MaterialRequestDTO createValidDTO() {
        return new MaterialRequestDTO(
                "Título válido",
                "Descrição válida",
                MaterialType.LIVRO,
                ConservationStatus.BOM,
                TransactionType.VENDA,
                50.0,
                true
        );
    }

    @Test
    void testValidDTO() {
        MaterialRequestDTO dto = createValidDTO();

        Set<ConstraintViolation<MaterialRequestDTO>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), "DTO válido não deve gerar violações");
    }

    @Test
    void testTitleIsBlank() {
        MaterialRequestDTO dto = createValidDTO();
        dto.setTitle("  ");

        Set<ConstraintViolation<MaterialRequestDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("O título é obrigatório")));
    }

    @Test
    void testMaterialTypeIsNull() {
        MaterialRequestDTO dto = createValidDTO();
        dto.setMaterialType(null);

        Set<ConstraintViolation<MaterialRequestDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("O tipo do material é obrigatório")));
    }

    @Test
    void testConservationStatusIsNull() {
        MaterialRequestDTO dto = createValidDTO();
        dto.setConservationStatus(null);

        Set<ConstraintViolation<MaterialRequestDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("O estado de conservação é obrigatório")));
    }

    @Test
    void testTransactionTypeIsNull() {
        MaterialRequestDTO dto = createValidDTO();
        dto.setTransactionType(null);

        Set<ConstraintViolation<MaterialRequestDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("O tipo de transação é obrigatório")));
    }

    @Test
    void testPriceIsNegative() {
        MaterialRequestDTO dto = createValidDTO();
        dto.setPrice(-10.0);

        Set<ConstraintViolation<MaterialRequestDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("O preço deve ser maior que zero")));
    }

    @Test
    void testGettersAndSetters() {
        MaterialRequestDTO dto = new MaterialRequestDTO();

        dto.setTitle("Novo título");
        dto.setDescription("Nova descrição");
        dto.setMaterialType(MaterialType.EQUIPAMENTO);
        dto.setConservationStatus(ConservationStatus.RAZOAVEL);
        dto.setTransactionType(TransactionType.TROCA);
        dto.setPrice(100.0);

        assertEquals("Novo título", dto.getTitle());
        assertEquals("Nova descrição", dto.getDescription());
        assertEquals(MaterialType.EQUIPAMENTO, dto.getMaterialType());
        assertEquals(ConservationStatus.RAZOAVEL, dto.getConservationStatus());
        assertEquals(TransactionType.TROCA, dto.getTransactionType());
        assertEquals(100.0, dto.getPrice());
    }
}
