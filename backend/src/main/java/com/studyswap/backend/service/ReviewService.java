package com.studyswap.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.studyswap.backend.dto.ReviewRequestDTO;
import com.studyswap.backend.model.Review;
import com.studyswap.backend.model.User;
import com.studyswap.backend.repository.AvaliacaoRepository;
import com.studyswap.backend.repository.TransactionRepository;

@Service
public class ReviewService {
    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private TransactionRepository transacaoRepository;

    @Transactional
    public Review createReview(ReviewRequestDTO dto, Authentication authentication){
        User user = (User) authentication.getPrincipal();

        var transacao = transacaoRepository.findByTransactionId(dto.getTransactionId());
        if (transacao == null) {
            throw new RuntimeException("Transacao nao realizada");
        }

        if (!transacao.getReceiver().getId().equals(user.getId())) {
            throw new SecurityException("Você não tem permissão para avaliar essa transação.");
        }

        if (avaliacaoRepository.findByTransacaoId(dto.getTransactionId()) != null) {
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
