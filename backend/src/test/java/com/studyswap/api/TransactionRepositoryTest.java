package com.studyswap.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.studyswap.backend.BackendApplication;
import com.studyswap.backend.model.Transaction;
import com.studyswap.backend.repository.TransactionRepository;

@SpringBootTest(classes = BackendApplication.class)
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    void listarTransacoes() {
        List<Transaction> transactions = transactionRepository.findAll();

        // Assert básico
        assertNotNull(transactions, "A lista de transações não deve ser nula");

        // Opcional: assert que a lista não está vazia (se houver dados de teste)
        // assertFalse(transactions.isEmpty(), "A lista de transações não deve estar vazia");

        // Mantém o print se quiser visualizar
        transactions.forEach(System.out::println);
    }
}
