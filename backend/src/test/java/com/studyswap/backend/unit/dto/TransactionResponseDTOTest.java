package com.studyswap.backend.unit.dto;

import com.studyswap.backend.dto.TransactionResponseDTO;
import com.studyswap.backend.model.TransactionStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TransactionResponseDTOTest {

    @Test
    void testConstructorWithAllArgs() {
        LocalDateTime now = LocalDateTime.now();
        TransactionResponseDTO dto = new TransactionResponseDTO(
                1L,
                now,
                100L,
                TransactionStatus.PENDING,
                10L,
                "Receiver Test",
                20L,
                "Announcer Test"
        );

        assertEquals(1L, dto.getId());
        assertEquals(now, dto.getTransactionDate());
        assertEquals(100L, dto.getIdMaterial());
        assertEquals(TransactionStatus.PENDING, dto.getStatus());
        assertEquals(10L, dto.getReceiverId());
        assertEquals("Receiver Test", dto.getReceiverName());
        assertEquals(20L, dto.getAnnouncerId());
        assertEquals("Announcer Test", dto.getAnnouncerName());
    }

    @Test
    void testSettersAndGetters() {
        TransactionResponseDTO dto = new TransactionResponseDTO(
                null, null, null, null, null, null, null, null
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

        assertEquals(2L, dto.getId());
        assertEquals(now, dto.getTransactionDate());
        assertEquals(200L, dto.getIdMaterial());
        assertEquals(TransactionStatus.CONCLUDED, dto.getStatus());
        assertEquals(30L, dto.getReceiverId());
        assertEquals("Receiver 2", dto.getReceiverName());
        assertEquals(40L, dto.getAnnouncerId());
        assertEquals("Announcer 2", dto.getAnnouncerName());
    }
}
