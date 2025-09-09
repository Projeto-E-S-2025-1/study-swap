package com.studyswap.backend.unit.service;

import com.studyswap.backend.dto.MaterialRequestDTO;
import com.studyswap.backend.dto.TransactionResponseDTO;
import com.studyswap.backend.model.ConservationStatus;
import com.studyswap.backend.model.Material;
import com.studyswap.backend.model.MaterialType;
import com.studyswap.backend.model.Transaction;
import com.studyswap.backend.model.TransactionStatus;
import com.studyswap.backend.model.TransactionType;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
        pendingTransaction = new Transaction(testMaterial, announcerUser, receiverUser, TransactionStatus.PENDING, TransactionType.DOACAO);
        pendingTransaction.setId(100L);

        concludedTransaction = new Transaction (testMaterial, announcerUser, receiverUser, TransactionStatus.CONCLUDED, TransactionType.DOACAO);
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
        TransactionResponseDTO result = transactionService.createTransaction(authentication, 10L, null);

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
            transactionService.createTransaction(authentication, 99L, null);
        });
    }

    @Test
    void testCreateTransaction_SelfTransaction() {
        when(authentication.getPrincipal()).thenReturn(announcerUser); // Mesmo usuário do material
        when(materialRepository.findById(10L)).thenReturn(Optional.of(testMaterial));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            transactionService.createTransaction(authentication, 10L, null);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testCreateTransaction_MaterialUnavailable() {
        testMaterial.setAvailable(false);
        when(authentication.getPrincipal()).thenReturn(receiverUser);
        when(materialRepository.findById(10L)).thenReturn(Optional.of(testMaterial));

        assertThrows(ResponseStatusException.class, () -> {
            transactionService.createTransaction(authentication, 10L, null);
        });
    }

    @Test
    void testCreateTransaction_Success_Exchange() {
        // Tipo de transação TROCA
        testMaterial.setTransactionType(TransactionType.TROCA);
        when(authentication.getPrincipal()).thenReturn(receiverUser);
        when(materialRepository.findById(10L)).thenReturn(Optional.of(testMaterial));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));

        // Material oferecido
        var offeredMaterialDTO = new MaterialRequestDTO();
        offeredMaterialDTO.setTitle("Livro Oferta");
        offeredMaterialDTO.setDescription("Descrição oferta");
        offeredMaterialDTO.setConservationStatus(ConservationStatus.NOVO);
        offeredMaterialDTO.setMaterialType(MaterialType.LIVRO);

        TransactionResponseDTO result = transactionService.createTransaction(authentication, 10L, offeredMaterialDTO);

        assertNotNull(result);
        assertEquals(TransactionStatus.PENDING, result.getStatus());
        assertEquals(TransactionType.TROCA, result.getTransactionType());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testCreateTransaction_Exchange_MissingOfferedMaterial() {
        // Tipo de transação TROCA
        testMaterial.setTransactionType(TransactionType.TROCA);
        when(authentication.getPrincipal()).thenReturn(receiverUser);
        when(materialRepository.findById(10L)).thenReturn(Optional.of(testMaterial));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            transactionService.createTransaction(authentication, 10L, null);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("É necessário enviar um material"));
    }

    // ---------------------- cancelTransaction ----------------------

   @Test
void testCancelTransaction_ByReceiver_Success() {
    lenient().when(authentication.getPrincipal()).thenReturn(receiverUser);

    doReturn(Optional.of(pendingTransaction.getMaterial()))
            .when(materialRepository).findById(anyLong());

    doReturn(List.of(pendingTransaction))
            .when(transactionRepository).findByMaterial(any(Material.class));

    transactionService.cancelTransaction(authentication, pendingTransaction.getMaterial().getId());

    verify(transactionRepository, times(1)).delete(pendingTransaction);
}


    @Test
    void testCancelTransaction_Forbidden() {
        lenient().when(authentication.getPrincipal()).thenReturn(anotherUser);

        doReturn(Optional.of(pendingTransaction.getMaterial()))
                .when(materialRepository).findById(anyLong());

        doReturn(List.of(pendingTransaction))
                .when(transactionRepository).findByMaterial(any(Material.class));

        Long materialId = pendingTransaction.getMaterial().getId();

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                transactionService.cancelTransaction(authentication, materialId)
        );

        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
        verify(transactionRepository, never()).delete(any());
    }

    @Test
    void testCancelTransaction_NotPending() {
        lenient().when(authentication.getPrincipal()).thenReturn(receiverUser);

        doReturn(Optional.of(concludedTransaction.getMaterial()))
                .when(materialRepository).findById(anyLong());

        doReturn(List.of(concludedTransaction))
                .when(transactionRepository).findByMaterial(any(Material.class));

        Long materialId = concludedTransaction.getMaterial().getId();

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                transactionService.cancelTransaction(authentication, materialId)
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        verify(transactionRepository, never()).delete(any());
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

    @Test
    void testCompleteTransaction_NotPending() {
        when(authentication.getPrincipal()).thenReturn(announcerUser);
        when(transactionRepository.findById(101L)).thenReturn(Optional.of(concludedTransaction));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            transactionService.completeTransaction(authentication, 101L);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("Só pode-se confirmar transação pendente"));
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

    @Test
    void testFindAllByMaterial_MaterialNotFound() {
        when(authentication.getPrincipal()).thenReturn(announcerUser);
        when(materialRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            transactionService.findAllTransactionsByMaterial(authentication, 99L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("Material não encontrado"));
    }

    private boolean invokeIsParticipantInTransaction(User user, Transaction transaction) throws Exception {
        var method = TransactionService.class.getDeclaredMethod("isParticipantInTransaction", User.class, Transaction.class);
        method.setAccessible(true);
        return (boolean) method.invoke(transactionService, user, transaction);
    }

    @Test
    void testIsParticipantInTransaction_FalseCase() throws Exception {
        // Usuário que não é nem announcer nem receiver
        boolean result = invokeIsParticipantInTransaction(anotherUser, pendingTransaction);

        assertFalse(result, "Usuário que não é announcer nem receiver não deve ser participante");
    }

    @Test
    void testIsParticipantInTransaction_TrueAsReceiver() throws Exception {
        // Usuário é o receiver da transação
        boolean result = invokeIsParticipantInTransaction(receiverUser, pendingTransaction);

        assertTrue(result, "Receiver deve ser considerado participante da transação");
    }

    @Test
    void testIsParticipantInTransaction_TrueAsAnnouncer() throws Exception {
        // Usuário é o announcer da transação
        boolean result = invokeIsParticipantInTransaction(announcerUser, pendingTransaction);

        assertTrue(result, "Announcer deve ser considerado participante da transação");
    }

    @Test
    void testIsParticipantInTransaction_NullUserOrIds() throws Exception {
        // transaction com announcer null
        Transaction t1 = new Transaction(testMaterial, null, receiverUser, TransactionStatus.PENDING, TransactionType.DOACAO);
        assertFalse(invokeIsParticipantInTransaction(announcerUser, t1));
        
        // transaction com receiver null
        Transaction t2 = new Transaction(testMaterial, announcerUser, null, TransactionStatus.PENDING, TransactionType.DOACAO);
        assertFalse(invokeIsParticipantInTransaction(receiverUser, t2));
        
        // user null
        assertFalse(invokeIsParticipantInTransaction(null, pendingTransaction));
        
        // IDs null
        User u = new User();
        u.setId(null);
        Transaction t3 = new Transaction(testMaterial, u, receiverUser, TransactionStatus.PENDING, TransactionType.DOACAO);
        assertFalse(invokeIsParticipantInTransaction(u, t3));
    }

    private boolean invokeIsReceiver(User user, Transaction transaction) throws Exception {
        var method = TransactionService.class.getDeclaredMethod("isReceiver", User.class, Transaction.class);
        method.setAccessible(true);
        return (boolean) method.invoke(transactionService, user, transaction);
    }

    @Test
    void testIsReceiver_AllBranches() throws Exception {
        User u1 = new User();
        u1.setId(1L);
        User u2 = new User();
        u2.setId(2L);

        Transaction tx = new Transaction();

        // Branch 1: receiver null
        tx.setReceiver(null);
        assertFalse(invokeIsReceiver(u1, tx));

        // Branch 2: user null
        tx.setReceiver(u1);
        assertFalse(invokeIsReceiver(null, tx));

        // Branch 3: receiver id null
        User u3 = new User(); // id null
        tx.setReceiver(u3);
        assertFalse(invokeIsReceiver(u1, tx));

        // Branch 4: receiver id igual ao user
        u3.setId(1L);
        tx.setReceiver(u3);
        assertTrue(invokeIsReceiver(u1, tx));

        // Branch 5: receiver id diferente do user
        User u4 = new User();
        u4.setId(99L);
        tx.setReceiver(u4);
        assertFalse(invokeIsReceiver(u1, tx));
    }

    @Test
    void testCompleteTransaction_WithMultiplePendingTransactions() {
        // Usuário anunciante
        when(authentication.getPrincipal()).thenReturn(announcerUser);

        // Outra transação pendente do mesmo material
        Transaction otherPending = new Transaction(testMaterial, announcerUser, anotherUser, TransactionStatus.PENDING, TransactionType.DOACAO);
        otherPending.setId(200L);

        // Outra transação concluída (não deve ser alterada)
        Transaction concludedTx = new Transaction(testMaterial, announcerUser, anotherUser, TransactionStatus.CONCLUDED, TransactionType.DOACAO);
        concludedTx.setId(300L);

        when(transactionRepository.findById(100L)).thenReturn(Optional.of(pendingTransaction));
        when(transactionRepository.findByMaterial(testMaterial)).thenReturn(List.of(pendingTransaction, otherPending, concludedTx));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));

        TransactionResponseDTO result = transactionService.completeTransaction(authentication, 100L);

        assertEquals(TransactionStatus.CONCLUDED, result.getStatus());
        assertEquals(TransactionStatus.DENIED, otherPending.getStatus());
        assertEquals(TransactionStatus.CONCLUDED, concludedTx.getStatus());
        assertFalse(testMaterial.isAvailable());
        verify(transactionRepository, times(2)).save(any(Transaction.class)); // pendingTransaction + otherPending + concludedTx (salvo mesmo que não alterado)
    }

    @Test
    void testCompleteTransaction_TransactionNotFound() {
        when(authentication.getPrincipal()).thenReturn(announcerUser);
        when(transactionRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                transactionService.completeTransaction(authentication, 999L));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void testCancelTransaction_MaterialNotFound() {
        when(materialRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                transactionService.cancelTransaction(authentication, 999L));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void testCancelTransaction_TransactionNotFound() {
        when(authentication.getPrincipal()).thenReturn(announcerUser);

        when(materialRepository.findById(10L)).thenReturn(Optional.of(testMaterial));

        when(transactionRepository.findByMaterial(any(Material.class)))
                .thenReturn(List.of());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                transactionService.cancelTransaction(authentication, 10L)
        );

        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
    }

    @Test
    void testFindAllTransactionsByUser_WithTransactions() {
        when(authentication.getPrincipal()).thenReturn(receiverUser);

        Transaction tx1 = pendingTransaction;
        Transaction tx2 = new Transaction(testMaterial, announcerUser, receiverUser, TransactionStatus.PENDING, TransactionType.DOACAO);
        tx2.setId(201L);

        when(transactionRepository.findByAnnouncer(receiverUser)).thenReturn(new ArrayList<>());
        when(transactionRepository.findByReceiver(receiverUser)).thenReturn(new ArrayList<>(List.of(tx1, tx2)));

        List<TransactionResponseDTO> result = transactionService.findAllTransactionsByUser(authentication);

        assertEquals(2, result.size());
        assertEquals(tx1.getId(), result.get(0).getId());
        assertEquals(tx2.getId(), result.get(1).getId());
    }
}