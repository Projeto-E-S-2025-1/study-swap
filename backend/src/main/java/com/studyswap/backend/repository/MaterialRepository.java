package com.studyswap.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.studyswap.backend.model.ConservationStatus;
import com.studyswap.backend.model.Material;
import com.studyswap.backend.model.MaterialType;
import com.studyswap.backend.model.TransactionType;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long>{
    @Query("SELECT m FROM Material m " +
           "WHERE (:title IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
           "AND (:materialType IS NULL OR m.materialType = :materialType) " +
           "AND (:conservationStatus IS NULL OR m.conservationStatus = :conservationStatus) " +
           "AND (:transactionType IS NULL OR m.transactionType = :transactionType)")
    List<Material> searchByFilters(
        @Param("title") String title,
        @Param("materialType") MaterialType materialType,
        @Param("conservationStatus") ConservationStatus conservationStatus,
        @Param("transactionType") TransactionType transactionType
    );
    List<Material> findByUser(com.studyswap.backend.model.User user);
    List<Material> findByAvailableTrue();
}
