package com.studyswap.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.studyswap.backend.model.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{
    Review findByTransactionId(Long transactionId);

    List<Review> findByTransaction_Receiver_Id(Long userId);
}
