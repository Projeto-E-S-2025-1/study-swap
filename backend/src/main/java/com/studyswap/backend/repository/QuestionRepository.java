package com.studyswap.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.studyswap.backend.model.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>{

}
