package com.studyswap.backend.unit.repository;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.studyswap.backend.model.ConservationStatus;
import com.studyswap.backend.model.Material;
import com.studyswap.backend.model.MaterialType;
import com.studyswap.backend.model.Review;
import com.studyswap.backend.model.Role;
import com.studyswap.backend.model.Transaction;
import com.studyswap.backend.model.TransactionStatus;
import com.studyswap.backend.model.TransactionType;
import com.studyswap.backend.model.User;
import com.studyswap.backend.repository.ReviewRepository;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName("ReviewRepository Integration Tests")
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User reviewerUser;
    private User announcerUser;
    private Material testMaterial;
    private Transaction testTransaction;

    @BeforeEach
    void setUp() {
        // Setup de dados de teste para as entidades dependentes
        reviewerUser = new User("Reviewer", "reviewer@example.com", "password", Role.STUDENT);
        announcerUser = new User("Announcer", "announcer@example.com", "password", Role.STUDENT);

        entityManager.persist(reviewerUser);
        entityManager.persist(announcerUser);

        testMaterial = new Material("Test Book", "A test book.", MaterialType.LIVRO,
                ConservationStatus.NOVO, TransactionType.VENDA, 10.0, "/images/book.png", announcerUser);
        entityManager.persist(testMaterial);

        testTransaction = new Transaction(testMaterial, announcerUser, reviewerUser, TransactionStatus.CONCLUDED, TransactionType.DOACAO);
        entityManager.persist(testTransaction);
        
        entityManager.flush();
    }

    @Test
    @DisplayName("Salvar uma avaliação")
    void testSaveReview() {
        // Cenário
        Review review = new Review(reviewerUser, 5, "Great material and quick delivery!", testTransaction);

        // Ação
        Review savedReview = reviewRepository.save(review);

        // Verificação
        assertThat(savedReview).isNotNull();
        assertThat(savedReview.getId()).isNotNull();
        assertThat(savedReview.getRating()).isEqualTo(5);
        assertThat(savedReview.getAuthor()).isEqualTo(reviewerUser);
    }

    @Test
    @DisplayName("Buscar avaliação por ID")
    void testFindById() {
        // Cenário
        Review review = new Review(reviewerUser, 4, "Material came as described.", testTransaction);
        Review savedReview = entityManager.persistAndFlush(review);

        // Ação
        Optional<Review> foundReview = reviewRepository.findById(savedReview.getId());

        // Verificação
        assertThat(foundReview).isPresent();
        assertThat(foundReview.get().getDescription()).isEqualTo("Material came as described.");
    }

    @Test
    @DisplayName("Deletar avaliação por ID")
    void testDeleteById() {
        // Cenário
        Review review = new Review(reviewerUser, 3, "Not as good as expected.", testTransaction);
        Review savedReview = entityManager.persistAndFlush(review);

        // Ação
        reviewRepository.deleteById(savedReview.getId());
        Optional<Review> deletedReview = reviewRepository.findById(savedReview.getId());

        // Verificação
        assertThat(deletedReview).isNotPresent();
    }

    @Test
    @DisplayName("Buscar avaliação por ID de transação")
    void testFindByTransactionId() {
        // Cenário
        Review review = new Review(reviewerUser, 5, "Perfect transaction!", testTransaction);
        entityManager.persistAndFlush(review);

        // Ação
        Review foundReview = reviewRepository.findByTransactionId(testTransaction.getId());

        // Verificação
        assertThat(foundReview).isNotNull();
        assertThat(foundReview.getDescription()).isEqualTo("Perfect transaction!");
        assertThat(foundReview.getRating()).isEqualTo(5);
    }

    @Test
    @DisplayName("Buscar avaliações pelo ID do usuário receiver")
    void testFindByTransactionReceiverId() {
        // Cria receiver (usuário que vai receber as reviews)
        User receiver = new User();
        receiver.setName("Destinatário");
        receiver.setEmail("receiver@test.com");
        receiver.setPassword("123456");
        receiver.setRole(Role.STUDENT);
        entityManager.persist(receiver);

        // Cria author (usuário que criou a review)
        User author = new User();
        author.setName("Autor");
        author.setEmail("author@test.com");
        author.setPassword("123456");
        author.setRole(Role.STUDENT);
        entityManager.persist(author);

        // Cria material obrigatório
        Material material = new Material();
        material.setTitle("Material Teste");                    
        material.setDescription("Descrição do material");
        material.setMaterialType(MaterialType.LIVRO);           
        material.setConservationStatus(ConservationStatus.NOVO); 
        material.setTransactionType(TransactionType.VENDA);     
        material.setPrice(10.0);
        material.setPhoto("/images/test.png");
        material.setUser(author);                                
        entityManager.persist(material);

        // Cria uma transação associada ao material
        Transaction transaction = new Transaction();
        transaction.setReceiver(receiver);
        transaction.setAnnouncer(author);
        transaction.setMaterial(material);                      
        transaction.setType(TransactionType.DOACAO);           
        transaction.setStatus(TransactionStatus.CONCLUDED);    
        entityManager.persist(transaction);

        // Cria review
        Review review = new Review();
        review.setTransaction(transaction);
        review.setAuthor(author);
        review.setDescription("Review da transação");
        review.setRating(5);
        entityManager.persist(review);

        entityManager.flush();

        // Testa query
        List<Review> reviews = reviewRepository.findByTransaction_Receiver_Id(receiver.getId());
        assertEquals(1, reviews.size());
        assertEquals("Review da transação", reviews.get(0).getDescription());
    }
}