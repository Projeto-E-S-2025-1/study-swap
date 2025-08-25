package com.studyswap.backend.unit.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.studyswap.backend.model.ConservationStatus;
import com.studyswap.backend.model.Material;
import com.studyswap.backend.model.MaterialType;
import com.studyswap.backend.model.Role;
import com.studyswap.backend.model.Transaction;
import com.studyswap.backend.model.TransactionStatus;
import com.studyswap.backend.model.TransactionType;
import com.studyswap.backend.model.User;
import com.studyswap.backend.repository.TransactionRepository;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName("TransactionRepository Integration Tests")
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User announcerUser;
    private User receiverUser;
    private Material testMaterial;
    private Material otherMaterial;

    @BeforeEach
    void setUp() {
        // Setup de dados de teste para as entidades dependentes
        announcerUser = new User("Announcer", "announcer@example.com", "password", Role.STUDENT);
        receiverUser = new User("Receiver", "receiver@example.com", "password", Role.STUDENT);
        entityManager.persist(announcerUser);
        entityManager.persist(receiverUser);

        testMaterial = new Material("Test Book", "A test book.", MaterialType.LIVRO,
                ConservationStatus.NOVO, TransactionType.VENDA, 10.0, "/images/book.png", announcerUser);
        entityManager.persist(testMaterial);

        otherMaterial = new Material("Old Equipment", "Used equipment.", MaterialType.EQUIPAMENTO,
                ConservationStatus.BOM, TransactionType.DOACAO, null, "/images/equipment.png", announcerUser);
        entityManager.persist(otherMaterial);

        entityManager.flush();
    }

    @Test
    @DisplayName("Salvar uma transação")
    void testSaveTransaction() {
        // Cenário
        Transaction transaction = new Transaction(null, testMaterial, announcerUser, receiverUser, TransactionStatus.PENDING);

        // Ação
        Transaction savedTransaction = transactionRepository.save(transaction);

        // Verificação
        assertThat(savedTransaction).isNotNull();
        assertThat(savedTransaction.getId()).isNotNull();
        assertThat(savedTransaction.getAnnouncer()).isEqualTo(announcerUser);
        assertThat(savedTransaction.getReceiver()).isEqualTo(receiverUser);
    }

    @Test
    @DisplayName("Buscar transação por ID")
    void testFindById() {
        // Cenário
        Transaction transaction = new Transaction(null, testMaterial, announcerUser, receiverUser, TransactionStatus.PENDING);
        Transaction savedTransaction = entityManager.persistAndFlush(transaction);

        // Ação
        Optional<Transaction> foundTransaction = transactionRepository.findById(savedTransaction.getId());

        // Verificação
        assertThat(foundTransaction).isPresent();
        assertThat(foundTransaction.get().getStatus()).isEqualTo(TransactionStatus.PENDING);
    }

    @Test
    @DisplayName("Deletar transação por ID")
    void testDeleteById() {
        // Cenário
        Transaction transaction = new Transaction(null, testMaterial, announcerUser, receiverUser, TransactionStatus.PENDING);
        Transaction savedTransaction = entityManager.persistAndFlush(transaction);

        // Ação
        transactionRepository.deleteById(savedTransaction.getId());
        Optional<Transaction> deletedTransaction = transactionRepository.findById(savedTransaction.getId());

        // Verificação
        assertThat(deletedTransaction).isNotPresent();
    }

    @Test
    @DisplayName("Buscar transações por status")
    void testFindByStatus() {
        // Cenário
        Transaction pendingTransaction = new Transaction(null, testMaterial, announcerUser, receiverUser,
                TransactionStatus.PENDING);
        Transaction concludedTransaction = new Transaction(null, otherMaterial, announcerUser, receiverUser,
                TransactionStatus.CONCLUDED);
        entityManager.persistAndFlush(pendingTransaction);
        entityManager.persistAndFlush(concludedTransaction);

        // Ação
        List<Transaction> pendingTransactions = transactionRepository.findByStatus(TransactionStatus.PENDING);
        List<Transaction> concludedTransactions = transactionRepository.findByStatus(TransactionStatus.CONCLUDED);

        // Verificação
        assertThat(pendingTransactions).hasSize(1);
        assertThat(pendingTransactions.get(0).getStatus()).isEqualTo(TransactionStatus.PENDING);

        assertThat(concludedTransactions).hasSize(1);
        assertThat(concludedTransactions.get(0).getStatus()).isEqualTo(TransactionStatus.CONCLUDED);
    }

    @Test
    @DisplayName("Buscar transações por material")
    void testFindByMaterial() {
        // Cenário
        Transaction transactionForTestMaterial1 = new Transaction(null, testMaterial, announcerUser, receiverUser, TransactionStatus.PENDING);
        Transaction transactionForTestMaterial2 = new Transaction(null, testMaterial, announcerUser, receiverUser, TransactionStatus.PENDING);
        Transaction transactionForOtherMaterial = new Transaction(null, otherMaterial, announcerUser, receiverUser, TransactionStatus.PENDING);
        entityManager.persistAndFlush(transactionForTestMaterial1);
        entityManager.persistAndFlush(transactionForTestMaterial2);
        entityManager.persistAndFlush(transactionForOtherMaterial);

        // Ação
        List<Transaction> transactionsForTestMaterial = transactionRepository.findByMaterial(testMaterial);

        // Verificação
        assertThat(transactionsForTestMaterial).hasSize(2);
        assertThat(transactionsForTestMaterial.stream().allMatch(t -> t.getMaterial().equals(testMaterial))).isTrue();
    }
}
