package com.studyswap.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.studyswap.backend.model.Material;
import com.studyswap.backend.model.Transaction;
import com.studyswap.backend.model.User;
import com.studyswap.backend.model.TransactionStatus;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{
    List<Transaction> findByStatus(TransactionStatus pending);
    List<Transaction> findByMaterial(Material material);
    List<Transaction> findByAnnouncer(User announcer);
    List<Transaction> findByReceiver(User receiver);
    Optional<Transaction> findByMaterialAndAnnouncer(Material material, User announcer);
}
