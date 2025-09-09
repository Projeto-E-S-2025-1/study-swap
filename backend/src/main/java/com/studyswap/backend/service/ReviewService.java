package com.studyswap.backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import java.util.List;

import com.studyswap.backend.dto.ReviewRequestDTO;
import com.studyswap.backend.dto.ReviewResponseDTO;
import com.studyswap.backend.model.Review;
import com.studyswap.backend.model.User;
import com.studyswap.backend.repository.ReviewRepository;
import com.studyswap.backend.repository.TransactionRepository;
import com.studyswap.backend.dto.UserReviewAverageDTO;

@Service
public class ReviewService {
    private ReviewRepository reviewRepository;
    private TransactionRepository transactionRepository;
    public ReviewService(
        ReviewRepository reviewRepository,
        TransactionRepository transactionRepository) {
        this.reviewRepository = reviewRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public ReviewResponseDTO createReview(ReviewRequestDTO dto, Authentication authentication){
        User user = (User) authentication.getPrincipal();

        var transacao = transactionRepository.findById(dto.getTransactionId())
        .orElseThrow(() -> new RuntimeException("Transação não realizada"));

        if (!transacao.getReceiver().getId().equals(user.getId())) {
            throw new SecurityException("Você não tem permissão para avaliar essa transação.");
        }

        if (reviewRepository.findByTransaction_Id(dto.getTransactionId()) != null) {
            throw new IllegalStateException("Essa transação já foi avaliada.");
        }

        var avaliacao = new Review(
            user,
            dto.getRating(),
            dto.getDescription(),
            transacao
        );

        Review review = reviewRepository.save(avaliacao);

        return convertToResponseDTO(review);
    }

    @Transactional
    public ReviewResponseDTO updateReview(Long avaliacaoId, ReviewRequestDTO dto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Review avaliacao = reviewRepository.findById(avaliacaoId)
            .orElseThrow(() -> new IllegalArgumentException("Avaliação não encontrada."));

        if (!avaliacao.getAuthor().getId().equals(user.getId())) {
            throw new SecurityException("Você não tem permissão para editar esta avaliação.");
        }

        avaliacao.setRating(dto.getRating());
        avaliacao.setDescription(dto.getDescription());

        Review review = reviewRepository.save(avaliacao);
        return convertToResponseDTO(review);
    }

    @Transactional
    public void deleteReview(Long avaliacaoId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Review avaliacao = reviewRepository.findById(avaliacaoId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Avaliação não encontrada"));

        if (!avaliacao.getAuthor().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para excluir esta avaliação");
        }

        reviewRepository.delete(avaliacao);
    }

    @Transactional
    public List<ReviewResponseDTO> getByUser(Long userId){
        List<Review> reviews = reviewRepository.findByTransaction_Announcer_Id(userId);
    
        return reviews.stream()
            .map(this::convertToResponseDTO)
            .toList();
    }

    @Transactional
    public ReviewResponseDTO getByTransactionId(Long transactionId){
        return this.convertToResponseDTO(reviewRepository.findByTransaction_Id(transactionId));
    }

    @Transactional
    public UserReviewAverageDTO getUserAverageRating(Long userId) {
        var reviews = reviewRepository.findByTransaction_Announcer_Id(userId);

        if (reviews.isEmpty()) {
            return new UserReviewAverageDTO(userId, 0.0, 0L);
        }

        double average = reviews.stream()
            .mapToInt(Review::getRating)
            .average()
            .orElse(0.0);

        return new UserReviewAverageDTO(userId, average, (long) reviews.size());
    }

    private ReviewResponseDTO convertToResponseDTO(Review review){
        return new ReviewResponseDTO(
            review.getId(),
            review.getAuthor().getName(),
            review.getAuthor().getId(),
            review.getTransaction().getMaterial().getTitle(),
            review.getRating(),
            review.getDescription(),
            review.getCreatedAt()
        );
    }
}
