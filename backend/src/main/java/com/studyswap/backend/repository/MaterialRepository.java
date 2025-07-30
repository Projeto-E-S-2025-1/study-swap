package com.studyswap.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.studyswap.backend.model.Material;
import com.studyswap.backend.model.MaterialType;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long>{
    @Query("SELECT m FROM Material m " +
           "WHERE (:title IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
           "AND (:materialType IS NULL OR m.materialType = :materialType)")
    List<Material> searchByTitleAndType(@Param("title") String title, @Param("materialType") MaterialType materialType);
}
