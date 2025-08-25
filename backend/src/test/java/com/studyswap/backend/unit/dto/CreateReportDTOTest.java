package com.studyswap.backend.unit.dto;

import com.studyswap.backend.dto.CreateReportDTO;
import com.studyswap.backend.model.ReportReason;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CreateReportDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidDTO() {
        CreateReportDTO dto = new CreateReportDTO(
                1L,
                2L,
                ReportReason.SPAM,
                "Conteúdo ofensivo"
        );

        Set<ConstraintViolation<CreateReportDTO>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), "Não deve haver violações para DTO válido");
    }

    @Test
    void testReasonIsNull() {
        CreateReportDTO dto = new CreateReportDTO(
                1L,
                2L,
                null,
                "Descrição qualquer"
        );

        Set<ConstraintViolation<CreateReportDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("O motivo da denúncia é obrigatório")));
    }

    @Test
    void testGettersAndSetters() {
        CreateReportDTO dto = new CreateReportDTO();

        dto.setReportedUserId(10L);
        dto.setReportedMaterialId(20L);
        dto.setReason(ReportReason.FRAUDE);
        dto.setDescription("Descrição teste");

        assertEquals(10L, dto.getReportedUserId());
        assertEquals(20L, dto.getReportedMaterialId());
        assertEquals(ReportReason.FRAUDE, dto.getReason());
        assertEquals("Descrição teste", dto.getDescription());
    }
}
