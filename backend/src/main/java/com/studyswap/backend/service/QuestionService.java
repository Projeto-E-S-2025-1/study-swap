package com.studyswap.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.studyswap.backend.dto.CreateQuestionDTO;
import com.studyswap.backend.dto.QuestionResponseDTO;
import com.studyswap.backend.dto.UpdateQuestionDTO;
import com.studyswap.backend.model.Question;
import com.studyswap.backend.model.User;
import com.studyswap.backend.repository.MaterialRepository;
import com.studyswap.backend.repository.QuestionRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
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

    public Question updateQuestion(Long questionId, UpdateQuestionDTO dto, Authentication authentication) {
        User loggedUser = (User) authentication.getPrincipal();

        Question question = questionRepository.findById(questionId)
            .orElseThrow(() -> new EntityNotFoundException("Pergunta não encontrada"));

        if (!question.getAuthor().getId().equals(loggedUser.getId())) {
            throw new SecurityException("Usuário não autorizado para atualizar esta pergunta");
        }

        if (dto.getTitle() != null) {
            question.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            question.setDescription(dto.getDescription());
        }

        return questionRepository.save(question);
    }

    public void deleteQuestion(Long questionId, Authentication authentication) {
        User loggedUser = (User) authentication.getPrincipal();

        Question question = questionRepository.findById(questionId)
            .orElseThrow(() -> new EntityNotFoundException("Pergunta não encontrada"));

        if (!question.getAuthor().getId().equals(loggedUser.getId())) {
            throw new SecurityException("Usuário não autorizado para deletar esta pergunta");
        }

        questionRepository.delete(question);
    }

    public List<QuestionResponseDTO> getQuestionsByMaterial(Long materialId) {
        if (!materialRepository.existsById(materialId)) {
            throw new EntityNotFoundException("Material não encontrado");
        }

        List<Question> questions = questionRepository.findByMaterialId(materialId);

        return questions.stream()
            .map(q -> new QuestionResponseDTO(
                q.getId(),
                q.getTitle(),
                q.getDescription(),
                q.getAuthor().getName(),
                q.getCreatedAt()
            ))
            .collect(Collectors.toList());
    }
}
