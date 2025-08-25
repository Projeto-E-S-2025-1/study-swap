package com.studyswap.backend.unit.model;

import org.junit.jupiter.api.Test;

import com.studyswap.backend.model.Material;
import com.studyswap.backend.model.Transaction;
import com.studyswap.backend.model.TransactionStatus;
import com.studyswap.backend.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {

    @Test
    void testTransactionConstructorAndGetters() {
        Material material = new Material();
        User announcer = new User();
        User receiver = new User();

        Transaction transaction = new Transaction(1L, material, announcer, receiver, TransactionStatus.PENDING);

        assertEquals(1L, transaction.getId());
        assertEquals(material, transaction.getMaterial());
        assertEquals(announcer, transaction.getAnnouncer());
        assertEquals(receiver, transaction.getReceiver());
        assertEquals(TransactionStatus.PENDING, transaction.getStatus());
    }

    @Test
    void testSettersAndGetters() {
        Material material = new Material();
        User announcer = new User();
        User receiver = new User();

        Transaction transaction = new Transaction();
        transaction.setId(10L);
        transaction.setMaterial(material);
        transaction.setAnnouncer(announcer);
        transaction.setReceiver(receiver);
        transaction.setStatus(TransactionStatus.CONCLUDED);

        assertEquals(10L, transaction.getId());
        assertEquals(material, transaction.getMaterial());
        assertEquals(announcer, transaction.getAnnouncer());
        assertEquals(receiver, transaction.getReceiver());
        assertEquals(TransactionStatus.CONCLUDED, transaction.getStatus());
    }

    @Test
    void testSetTransactionDateManually() {
        Transaction transaction = new Transaction();
        LocalDateTime now = LocalDateTime.now();

        transaction.setTransactionDate(now);

        assertEquals(now, transaction.getTransactionDate());
    }
}
