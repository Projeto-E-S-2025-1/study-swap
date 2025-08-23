package com.studyswap.backend.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.studyswap.backend.dto.ReviewRequestDTO;
import com.studyswap.backend.model.Review;
import com.studyswap.backend.model.User;
import com.studyswap.backend.repository.ReviewRepository;
import com.studyswap.backend.repository.TransactionRepository;

@Service
public class ReviewService {
    private ReviewRepository avaliacaoRepository;

    private TransactionRepository transacaoRepository;

    public ReviewService(ReviewRepository avaliacaoRepository, TransactionRepository transacaoRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.transacaoRepository = transacaoRepository;
    }

    @Transactional
    public Review createReview(ReviewRequestDTO dto, Authentication authentication){
        User user = (User) authentication.getPrincipal();

        var transacao = transacaoRepository.findById(dto.getTransactionId())
        .orElseThrow(() -> new RuntimeException("Transação não realizada"));

        if (!transacao.getReceiver().getId().equals(user.getId())) {
            throw new SecurityException("Você não tem permissão para avaliar essa transação.");
        }

        if (avaliacaoRepository.findByTransactionId(dto.getTransactionId()) != null) {
            throw new IllegalStateException("Essa transação já foi avaliada.");
        }

        var avaliacao = new Review(
            user,
            dto.getRating(),
            dto.getDescription(),
            transacao
        );

        return avaliacaoRepository.save(avaliacao);
    }

    @Transactional
    public Review updateReview(Long avaliacaoId, ReviewRequestDTO dto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Review avaliacao = avaliacaoRepository.findById(avaliacaoId)
            .orElseThrow(() -> new IllegalArgumentException("Avaliação não encontrada."));

        if (!avaliacao.getAuthor().getId().equals(user.getId())) {
            throw new SecurityException("Você não tem permissão para editar esta avaliação.");
        }

        avaliacao.setRating(dto.getRating());
        avaliacao.setDescription(dto.getDescription());

        return avaliacaoRepository.save(avaliacao);
    }

    @Transactional
    public void deleteReview(Long avaliacaoId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Review avaliacao = avaliacaoRepository.findById(avaliacaoId)
            .orElseThrow(() -> new IllegalArgumentException("Avaliação não encontrada."));

        if (!avaliacao.getAuthor().getId().equals(user.getId())) {
            throw new SecurityException("Você não tem permissão para excluir esta avaliação.");
        }

        avaliacaoRepository.delete(avaliacao);
    }
}
