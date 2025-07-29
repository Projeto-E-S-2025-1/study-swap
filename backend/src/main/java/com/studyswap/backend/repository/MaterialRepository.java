package com.studyswap.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.studyswap.backend.model.Material;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long>{
    
}
