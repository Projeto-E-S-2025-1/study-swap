package com.studyswap.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import com.studyswap.backend.dto.CreateQuestionDTO;

import com.studyswap.backend.model.Question;
import com.studyswap.backend.model.User;

import com.studyswap.backend.repository.MaterialRepository;
import com.studyswap.backend.repository.QuestionRepository;

public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private MaterialRepository materialRepository;

    public Question createQuestion(CreateQuestionDTO dto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        var material = materialRepository.findById(dto.getMaterialId())
            .orElseThrow(() -> new RuntimeException("Material não encontrado"));

        var question = new Question(
            null,
            dto.getDescription(),
            dto.getTitle(),
            user,
            material
        );

        return questionRepository.save(question);
    }

}
