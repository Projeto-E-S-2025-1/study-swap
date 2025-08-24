package com.studyswap.backend.unit.service;

import com.studyswap.backend.dto.CreateQuestionDTO;
import com.studyswap.backend.dto.QuestionResponseDTO;
import com.studyswap.backend.dto.UpdateQuestionDTO;
import com.studyswap.backend.model.Material;
import com.studyswap.backend.model.Question;
import com.studyswap.backend.model.User;
import com.studyswap.backend.repository.MaterialRepository;
import com.studyswap.backend.repository.QuestionRepository;
import com.studyswap.backend.service.QuestionService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private MaterialRepository materialRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private QuestionService questionService;

    private User authorUser;
    private User anotherUser;
    private Material testMaterial;
    private Question testQuestion;
    private CreateQuestionDTO createDto;
    private UpdateQuestionDTO updateDto;

    @BeforeEach
    void setUp() {
        // Usuários
        authorUser = new User("Author User", "author@example.com", "password", null);
        authorUser.setId(1L);

        anotherUser = new User("Another User", "another@example.com", "password", null);
        anotherUser.setId(2L);

        // Material
        testMaterial = new Material();
        testMaterial.setId(10L);
        testMaterial.setTitle("Livro de Cálculo");

        // DTOs
        createDto = new CreateQuestionDTO();
        createDto.setMaterialId(10L);
        createDto.setTitle("Dúvida sobre Derivadas");
        createDto.setDescription("Como resolver a derivada de x^2?");

        updateDto = new UpdateQuestionDTO();
        updateDto.setTitle("Dúvida sobre Derivadas (Editado)");
        updateDto.setDescription("Como resolver a derivada de x^3?");

        // Entidade Pergunta (Question)
        testQuestion = new Question(1L, "Dúvida sobre Derivadas",
                "Como resolver a derivada de x^2?", authorUser, testMaterial);
        testQuestion.setCreatedAt(LocalDateTime.now());
    }

    // ---------------------- createQuestion ----------------------

    @Test
    void testCreateQuestion_Success() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(authorUser);
        when(materialRepository.findById(10L)).thenReturn(Optional.of(testMaterial));
        when(questionRepository.save(any(Question.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Question result = questionService.createQuestion(createDto, authentication);

        // Assert
        assertNotNull(result);
        assertEquals(createDto.getTitle(), result.getTitle());
        assertEquals(createDto.getDescription(), result.getDescription()); // Boa prática adicionar
        assertEquals(authorUser.getId(), result.getAuthor().getId());
        verify(materialRepository, times(1)).findById(10L);
        verify(questionRepository, times(1)).save(any(Question.class));
    }

    @Test
    void testCreateQuestion_MaterialNotFound() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(authorUser);
        when(materialRepository.findById(99L)).thenReturn(Optional.empty());
        createDto.setMaterialId(99L);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            questionService.createQuestion(createDto, authentication);
        });
    }

    // ---------------------- updateQuestion ----------------------

    @Test
    void testUpdateQuestion_Success() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(authorUser);
        when(questionRepository.findById(1L)).thenReturn(Optional.of(testQuestion));
        when(questionRepository.save(any(Question.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Question result = questionService.updateQuestion(1L, updateDto, authentication);

        // Assert
        assertNotNull(result);
        assertEquals(updateDto.getTitle(), result.getTitle());
        assertEquals(updateDto.getDescription(), result.getDescription());
        verify(questionRepository, times(1)).findById(1L);
        verify(questionRepository, times(1)).save(any(Question.class));
    }

    @Test
    void testUpdateQuestion_NotFound() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(authorUser);
        when(questionRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            questionService.updateQuestion(99L, updateDto, authentication);
        });
    }

    @Test
    void testUpdateQuestion_Forbidden() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(anotherUser); 
        when(questionRepository.findById(1L)).thenReturn(Optional.of(testQuestion));

        // Act & Assert
        assertThrows(SecurityException.class, () -> {
            questionService.updateQuestion(1L, updateDto, authentication);
        });
    }

    // ---------------------- deleteQuestion ----------------------

    @Test
    void testDeleteQuestion_Success() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(authorUser);
        when(questionRepository.findById(1L)).thenReturn(Optional.of(testQuestion));
        doNothing().when(questionRepository).delete(testQuestion);

        // Act
        questionService.deleteQuestion(1L, authentication);

        // Assert
        verify(questionRepository, times(1)).findById(1L);
        verify(questionRepository, times(1)).delete(testQuestion);
    }

    @Test
    void testDeleteQuestion_Forbidden() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(anotherUser); 
        when(questionRepository.findById(1L)).thenReturn(Optional.of(testQuestion));

        // Act & Assert
        assertThrows(SecurityException.class, () -> {
            questionService.deleteQuestion(1L, authentication);
        });
        verify(questionRepository, never()).delete(any(Question.class));
    }

    // ---------------------- getQuestionsByMaterial ----------------------

    @Test
    void testGetQuestionsByMaterial_Success() {
        // Arrange
        when(materialRepository.existsById(10L)).thenReturn(true);
        when(questionRepository.findByMaterialId(10L)).thenReturn(List.of(testQuestion));

        // Act
        List<QuestionResponseDTO> result = questionService.getQuestionsByMaterial(10L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testQuestion.getTitle(), result.get(0).getTitle());
        assertEquals(authorUser.getName(), result.get(0).getAuthorName());
    }

    @Test
    void testGetQuestionsByMaterial_MaterialNotFound() {
        // Arrange
        when(materialRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            questionService.getQuestionsByMaterial(99L);
        });
    }
}