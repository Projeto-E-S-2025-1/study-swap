package com.studyswap.api;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.studyswap.backend.BackendApplication;
import com.studyswap.backend.model.Transaction;
import com.studyswap.backend.repository.TransactionRepository;

@SpringBootTest(classes = BackendApplication.class)
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    public void listarTransacoes() {
        List<Transaction> transactions = transactionRepository.findAll();
        transactions.forEach(System.out::println);
    }
}
