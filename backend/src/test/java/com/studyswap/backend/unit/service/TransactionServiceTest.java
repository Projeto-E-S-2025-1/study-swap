package com.studyswap.backend.unit.service;

import com.studyswap.backend.dto.TransactionResponseDTO;
import com.studyswap.backend.model.Material;
import com.studyswap.backend.model.Transaction;
import com.studyswap.backend.model.TransactionStatus;
import com.studyswap.backend.model.User;
import com.studyswap.backend.repository.MaterialRepository;
import com.studyswap.backend.repository.TransactionRepository;
import com.studyswap.backend.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private MaterialRepository materialRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private TransactionService transactionService;

    private User announcerUser;
    private User receiverUser;
    private User anotherUser;
    private Material testMaterial;
    private Transaction pendingTransaction;
    private Transaction concludedTransaction;

    @BeforeEach
    void setUp() {
        // Usuários
        announcerUser = new User("Announcer", "announcer@example.com", "pass", null);
        announcerUser.setId(1L);

        receiverUser = new User("Receiver", "receiver@example.com", "pass", null);
        receiverUser.setId(2L);

        anotherUser = new User("Another", "another@example.com", "pass", null);
        anotherUser.setId(3L);

        // Material
        testMaterial = new Material();
        testMaterial.setId(10L);
        testMaterial.setUser(announcerUser);
        testMaterial.setAvailable(true);

        // Transações
        pendingTransaction = new Transaction(10L, testMaterial, announcerUser, receiverUser, TransactionStatus.PENDING);
        pendingTransaction.setId(100L);

        concludedTransaction = new Transaction(10L, testMaterial, announcerUser, receiverUser, TransactionStatus.CONCLUDED);
        concludedTransaction.setId(101L);
    }

    // ---------------------- createTransaction ----------------------

    @Test
    void testCreateTransaction_Success() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(receiverUser);
        when(materialRepository.findById(10L)).thenReturn(Optional.of(testMaterial));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(pendingTransaction);

        // Act
        TransactionResponseDTO result = transactionService.createTransaction(authentication, 10L);

        // Assert
        assertNotNull(result);
        assertEquals(pendingTransaction.getId(), result.getId());
        assertEquals(TransactionStatus.PENDING, result.getStatus());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testCreateTransaction_MaterialNotFound() {
        when(materialRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            transactionService.createTransaction(authentication, 99L);
        });
    }

    @Test
    void testCreateTransaction_SelfTransaction() {
        when(authentication.getPrincipal()).thenReturn(announcerUser); // Mesmo usuário do material
        when(materialRepository.findById(10L)).thenReturn(Optional.of(testMaterial));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            transactionService.createTransaction(authentication, 10L);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testCreateTransaction_MaterialUnavailable() {
        testMaterial.setAvailable(false);
        when(authentication.getPrincipal()).thenReturn(receiverUser);
        when(materialRepository.findById(10L)).thenReturn(Optional.of(testMaterial));

        assertThrows(ResponseStatusException.class, () -> {
            transactionService.createTransaction(authentication, 10L);
        });
    }

    // ---------------------- cancelTransaction ----------------------

    @Test
    void testCancelTransaction_ByReceiver_Success() {
        when(authentication.getPrincipal()).thenReturn(receiverUser);
        when(transactionRepository.findById(100L)).thenReturn(Optional.of(pendingTransaction));
        doNothing().when(transactionRepository).delete(pendingTransaction);

        transactionService.cancelTransaction(authentication, 100L);

        verify(transactionRepository, times(1)).delete(pendingTransaction);
    }

    @Test
    void testCancelTransaction_Forbidden() {
        when(authentication.getPrincipal()).thenReturn(anotherUser);
        when(transactionRepository.findById(100L)).thenReturn(Optional.of(pendingTransaction));

        assertThrows(ResponseStatusException.class, () -> {
            transactionService.cancelTransaction(authentication, 100L);
        });
    }

    @Test
    void testCancelTransaction_NotPending() {
        when(authentication.getPrincipal()).thenReturn(receiverUser);
        when(transactionRepository.findById(101L)).thenReturn(Optional.of(concludedTransaction));

        assertThrows(ResponseStatusException.class, () -> {
            transactionService.cancelTransaction(authentication, 101L);
        });
    }

    // ---------------------- completeTransaction ----------------------

    @Test
    void testCompleteTransaction_Success() {
        when(authentication.getPrincipal()).thenReturn(announcerUser);
        when(transactionRepository.findById(100L)).thenReturn(Optional.of(pendingTransaction));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));

        TransactionResponseDTO result = transactionService.completeTransaction(authentication, 100L);

        assertNotNull(result);
        assertEquals(TransactionStatus.CONCLUDED, result.getStatus());
        assertFalse(testMaterial.isAvailable()); // Verifica se o material foi marcado como indisponível
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testCompleteTransaction_Forbidden() {
        when(authentication.getPrincipal()).thenReturn(receiverUser); // Apenas o anunciante pode completar
        when(transactionRepository.findById(100L)).thenReturn(Optional.of(pendingTransaction));

        assertThrows(ResponseStatusException.class, () -> {
            transactionService.completeTransaction(authentication, 100L);
        });
    }

    // ---------------------- findAllTransactionsByMaterial ----------------------

    @Test
    void testFindAllByMaterial_Success() {
        when(authentication.getPrincipal()).thenReturn(announcerUser);
        when(materialRepository.findById(10L)).thenReturn(Optional.of(testMaterial));
        when(transactionRepository.findByMaterial(testMaterial)).thenReturn(List.of(pendingTransaction));

        List<TransactionResponseDTO> result = transactionService.findAllTransactionsByMaterial(authentication, 10L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(pendingTransaction.getId(), result.get(0).getId());
    }

    @Test
    void testFindAllByMaterial_Forbidden() {
        when(authentication.getPrincipal()).thenReturn(receiverUser); // Não é o dono do material
        when(materialRepository.findById(10L)).thenReturn(Optional.of(testMaterial));

        assertThrows(ResponseStatusException.class, () -> {
            transactionService.findAllTransactionsByMaterial(authentication, 10L);
        });
    }
}