package com.studyswap.backend.unit.dto;

import com.studyswap.backend.dto.ReportResponseDTO;
import com.studyswap.backend.model.ReportReason;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReportResponseDTOTest {

    @Test
    void testConstructorWithAllArgs() {
        ReportResponseDTO dto = new ReportResponseDTO(
                1L,
                2L,
                3L,
                4L,
                ReportReason.SPAM,
                "Descrição da denúncia"
        );

        assertEquals(1L, dto.getId());
        assertEquals(2L, dto.getReporterId());
        assertEquals(3L, dto.getReportedUserId());
        assertEquals(4L, dto.getReportedMaterialId());
        assertEquals(ReportReason.SPAM, dto.getReason());
        assertEquals("Descrição da denúncia", dto.getDescription());
    }

    @Test
    void testSettersAndGetters() {
        ReportResponseDTO dto = new ReportResponseDTO();

        dto.setId(10L);
        dto.setReporterId(20L);
        dto.setReportedUserId(30L);
        dto.setReportedMaterialId(40L);
        dto.setReason(ReportReason.FRAUDE);
        dto.setDescription("Nova descrição");

        assertEquals(10L, dto.getId());
        assertEquals(20L, dto.getReporterId());
        assertEquals(30L, dto.getReportedUserId());
        assertEquals(40L, dto.getReportedMaterialId());
        assertEquals(ReportReason.FRAUDE, dto.getReason());
        assertEquals("Nova descrição", dto.getDescription());
    }
}
