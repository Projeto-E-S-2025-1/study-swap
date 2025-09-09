package com.studyswap.backend.unit.dto;

import com.studyswap.backend.dto.MaterialDTO;
import com.studyswap.backend.dto.TransactionResponseDTO;
import com.studyswap.backend.model.ConservationStatus;
import com.studyswap.backend.model.MaterialType;
import com.studyswap.backend.model.TransactionStatus;
import com.studyswap.backend.model.TransactionType;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TransactionResponseDTOTest {

    @Test
    void testConstructorWithAllArgs() {
        LocalDateTime now = LocalDateTime.now();
        MaterialDTO offeredMaterial = new MaterialDTO(1L, "Material Test", MaterialType.LIVRO, ConservationStatus.BOM);
        TransactionResponseDTO dto = new TransactionResponseDTO(
                1L,
                now,
                100L,
                "Material Test",
                TransactionStatus.PENDING,
                10L,
                "Receiver Test",
                20L,
                "Announcer Test",
                TransactionType.DOACAO,
                offeredMaterial
        );

        assertEquals(1L, dto.getId());
        assertEquals(now, dto.getTransactionDate());
        assertEquals(100L, dto.getIdMaterial());
        assertEquals(TransactionStatus.PENDING, dto.getStatus());
        assertEquals(10L, dto.getReceiverId());
        assertEquals("Receiver Test", dto.getReceiverName());
        assertEquals(20L, dto.getAnnouncerId());
        assertEquals(TransactionType.DOACAO, dto.getTransactionType());
        assertEquals("Announcer Test", dto.getAnnouncerName());
        assertEquals(offeredMaterial, dto.getOfferedMaterial());
    }

    @Test
    void testSettersAndGetters() {
        TransactionResponseDTO dto = new TransactionResponseDTO(
                null, null, null, null, null, null, null, null, null, null, null
        );

        LocalDateTime now = LocalDateTime.now();

        dto.setId(2L);
        dto.setTransactionDate(now);
        dto.setIdMaterial(200L);
        dto.setStatus(TransactionStatus.CONCLUDED);
        dto.setReceiverId(30L);
        dto.setReceiverName("Receiver 2");
        dto.setAnnouncerId(40L);
        dto.setAnnouncerName("Announcer 2");
        dto.setTransactionType(TransactionType.TROCA);
        MaterialDTO offeredMaterial = new MaterialDTO(2L, "Material Test 2", MaterialType.APOSTILA, ConservationStatus.NOVO);
        dto.setOfferedMaterial(offeredMaterial);

        assertEquals(2L, dto.getId());
        assertEquals(now, dto.getTransactionDate());
        assertEquals(200L, dto.getIdMaterial());
        assertEquals(TransactionStatus.CONCLUDED, dto.getStatus());
        assertEquals(30L, dto.getReceiverId());
        assertEquals("Receiver 2", dto.getReceiverName());
        assertEquals(40L, dto.getAnnouncerId());
        assertEquals("Announcer 2", dto.getAnnouncerName());
        assertEquals(TransactionType.TROCA, dto.getTransactionType());
        assertEquals(offeredMaterial, dto.getOfferedMaterial());
    }
}
