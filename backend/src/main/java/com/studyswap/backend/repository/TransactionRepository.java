package com.studyswap.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.studyswap.backend.model.Material;
import com.studyswap.backend.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{
    List<Transaction> findByStatus(String status);
    List<Transaction> findByMaterial(Material material);
}
