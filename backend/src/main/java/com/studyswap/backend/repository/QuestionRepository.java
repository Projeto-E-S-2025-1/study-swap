package com.studyswap.backend.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.studyswap.backend.model.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>{
    List<Question> findByMaterialId(Long materialId);
}
