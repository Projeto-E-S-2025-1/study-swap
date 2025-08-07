package com.studyswap.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.studyswap.backend.model.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{
    Review findByTransacaoId(Long avaliacaoId);
}
