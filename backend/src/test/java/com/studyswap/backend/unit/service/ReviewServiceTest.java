package com.studyswap.backend.unit.service;

import com.studyswap.backend.dto.ReviewRequestDTO;
import com.studyswap.backend.model.Review;
import com.studyswap.backend.model.Transaction;
import com.studyswap.backend.model.User;
import com.studyswap.backend.repository.ReviewRepository;
import com.studyswap.backend.repository.TransactionRepository;
import com.studyswap.backend.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ReviewService reviewService;

    private User transactionProvider;
    private User transactionReceiver;
    private Transaction testTransaction;
    private Review testReview;
    private ReviewRequestDTO createDto;
    private ReviewRequestDTO updateDto;

    @BeforeEach
    void setUp() {
        // Usuários
        transactionProvider = new User("Provider", "provider@example.com", "pass", null);
        transactionProvider.setId(1L);

        transactionReceiver = new User("Receiver", "receiver@example.com", "pass", null);
        transactionReceiver.setId(2L);

        // Transação
        testTransaction = new Transaction();
        testTransaction.setId(10L);
        testTransaction.setReceiver(transactionReceiver);

        // Avaliação (Review)
        testReview = new Review(transactionReceiver, 5, "Ótimo material!", testTransaction);
        testReview.setId(100L);

        // DTOs
        createDto = new ReviewRequestDTO(10L, 5, "Ótimo material!");

        updateDto = new ReviewRequestDTO();
        updateDto.setRating(4);
        updateDto.setDescription("Material bom, mas com algumas anotações.");
    }

    // ---------------------- createReview ----------------------

    @Test
    void testCreateReview_Success() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(transactionReceiver);
        when(transactionRepository.findById(10L)).thenReturn(Optional.of(testTransaction));
        when(reviewRepository.findByTransactionId(10L)).thenReturn(null);
        when(reviewRepository.save(any(Review.class))).thenReturn(testReview);

        // Act
        Review result = reviewService.createReview(createDto, authentication);

        // Assert
        assertNotNull(result);
        assertEquals(createDto.getRating(), result.getRating());
        assertEquals(transactionReceiver.getId(), result.getAuthor().getId());
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void testCreateReview_TransactionNotFound() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(transactionReceiver);
        when(transactionRepository.findById(99L)).thenReturn(Optional.empty());
        ReviewRequestDTO dtoWithWrongId = new ReviewRequestDTO(99L, 5, "Test");

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            reviewService.createReview(dtoWithWrongId, authentication);
        });
    }

    @Test
    void testCreateReview_Forbidden() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(transactionProvider); // Usuário errado tentando avaliar
        when(transactionRepository.findById(10L)).thenReturn(Optional.of(testTransaction));

        // Act & Assert
        assertThrows(SecurityException.class, () -> {
            reviewService.createReview(createDto, authentication);
        });
    }

    @Test
    void testCreateReview_AlreadyExists() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(transactionReceiver);
        when(transactionRepository.findById(10L)).thenReturn(Optional.of(testTransaction));
        when(reviewRepository.findByTransactionId(10L)).thenReturn(testReview); // Simula que já existe

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            reviewService.createReview(createDto, authentication);
        });
    }

    // ---------------------- updateReview ----------------------

    @Test
    void testUpdateReview_Success() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(transactionReceiver);
        when(reviewRepository.findById(100L)).thenReturn(Optional.of(testReview));
        when(reviewRepository.save(any(Review.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Review result = reviewService.updateReview(100L, updateDto, authentication);

        // Assert
        assertNotNull(result);
        assertEquals(updateDto.getRating(), result.getRating());
        assertEquals(updateDto.getDescription(), result.getDescription());
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void testUpdateReview_NotFound() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(transactionReceiver);
        when(reviewRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            reviewService.updateReview(99L, updateDto, authentication);
        });
    }

    @Test
    void testUpdateReview_Forbidden() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(transactionProvider); // Usuário errado
        when(reviewRepository.findById(100L)).thenReturn(Optional.of(testReview));

        // Act & Assert
        assertThrows(SecurityException.class, () -> {
            reviewService.updateReview(100L, updateDto, authentication);
        });
    }

    // ---------------------- deleteReview ----------------------

    @Test
    void testDeleteReview_Success() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(transactionReceiver);
        when(reviewRepository.findById(100L)).thenReturn(Optional.of(testReview));
        doNothing().when(reviewRepository).delete(testReview);

        // Act
        reviewService.deleteReview(100L, authentication);

        // Assert
        verify(reviewRepository, times(1)).delete(testReview);
    }



    @Test
    void testDeleteReview_Forbidden() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(transactionProvider); // Usuário errado
        when(reviewRepository.findById(100L)).thenReturn(Optional.of(testReview));

        // Act & Assert
        assertThrows(SecurityException.class, () -> {
            reviewService.deleteReview(100L, authentication);
        });
        verify(reviewRepository, never()).delete(any());
    }
}