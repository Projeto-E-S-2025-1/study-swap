package com.studyswap.backend.unit.model;

import org.junit.jupiter.api.Test;

import com.studyswap.backend.model.Material;
import com.studyswap.backend.model.Transaction;
import com.studyswap.backend.model.TransactionStatus;
import com.studyswap.backend.model.TransactionType;
import com.studyswap.backend.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void testTransactionConstructorAndGetters() {
        Material material = new Material();
        User announcer = new User();
        User receiver = new User();

        Transaction transaction = new Transaction(material, announcer, receiver, TransactionStatus.PENDING, TransactionType.DOACAO);

        assertEquals(material, transaction.getMaterial());
        assertEquals(announcer, transaction.getAnnouncer());
        assertEquals(receiver, transaction.getReceiver());
        assertEquals(TransactionStatus.PENDING, transaction.getStatus());
        assertEquals(TransactionType.DOACAO, transaction.getType());
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
        transaction.setType(TransactionType.TROCA);

        assertEquals(10L, transaction.getId());
        assertEquals(material, transaction.getMaterial());
        assertEquals(announcer, transaction.getAnnouncer());
        assertEquals(receiver, transaction.getReceiver());
        assertEquals(TransactionStatus.CONCLUDED, transaction.getStatus());
        assertEquals(TransactionType.TROCA, transaction.getType());
    }

    @Test
    void testSetTransactionDateManually() {
        Transaction transaction = new Transaction();
        LocalDateTime now = LocalDateTime.now();

        transaction.setTransactionDate(now);

        assertEquals(now, transaction.getTransactionDate());
    }
}
