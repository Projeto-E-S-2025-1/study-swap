package com.studyswap.backend.unit.model;

import com.studyswap.backend.model.Review;
import com.studyswap.backend.model.Transaction;
import com.studyswap.backend.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReviewTest {

    @Test
    void testGettersAndSetters() {
        // Mock User e Transaction
        User author = new User();
        author.setId(1L);
        author.setName("Usuário Teste");
        author.setEmail("teste@email.com");

        Transaction transaction = new Transaction();
        transaction.setId(10L);

        // Criar Review usando construtor
        Review review = new Review(author, 4, "Ótimo produto", transaction);

        // Verificar construtor
        assertEquals(author, review.getAuthor());
        assertEquals(4, review.getRating());
        assertEquals("Ótimo produto", review.getDescription());
        assertEquals(transaction, review.getTransaction());
        assertNull(review.getId()); 
        assertNull(review.getCreatedAt());

        // Testar setters
        review.setId(100L);
        User newAuthor = new User();
        newAuthor.setId(2L);
        review.setAuthor(newAuthor);
        review.setRating(5);
        review.setDescription("Atualizado");
        Transaction newTransaction = new Transaction();
        newTransaction.setId(20L);
        review.setTransaction(newTransaction);

        // Testar getters
        assertEquals(100L, review.getId());
        assertEquals(newAuthor, review.getAuthor());
        assertEquals(5, review.getRating());
        assertEquals("Atualizado", review.getDescription());
        assertEquals(newTransaction, review.getTransaction());
    }

    @Test
    void testDefaultConstructor() {
        Review review = new Review();
        assertNotNull(review);
    }
}
