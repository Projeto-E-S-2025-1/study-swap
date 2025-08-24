package com.studyswap.backend.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyswap.backend.controller.QuestionController;
import com.studyswap.backend.dto.CreateQuestionDTO;
import com.studyswap.backend.dto.QuestionResponseDTO;
import com.studyswap.backend.dto.UpdateQuestionDTO;
import com.studyswap.backend.model.Material;
import com.studyswap.backend.model.Question;
import com.studyswap.backend.model.Role;
import com.studyswap.backend.model.User;
import com.studyswap.backend.service.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class QuestionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private QuestionService questionService;

    @InjectMocks
    private QuestionController questionController;

    private ObjectMapper objectMapper;
    private User testUser;
    private Material testMaterial;
    private Question testQuestion;
    private Authentication mockAuth;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        // The MockMvc will not handle exceptions, so we'll expect the raw exceptions
        mockMvc = MockMvcBuilders.standaloneSetup(questionController).build();

        testUser = new User();
        testUser.setId(1L);
        testUser.setName("João da Silva");
        testUser.setRole(Role.STUDENT);

        testMaterial = new Material();

        testQuestion = new Question(1L,
                "Onde posso encontrar mais exercícios sobre o capítulo 5?",
                "Dúvida sobre o livro de física",
                testUser,
                testMaterial);
        testQuestion.setCreatedAt(LocalDateTime.now());

        mockAuth = new UsernamePasswordAuthenticationToken(testUser, null, Collections.emptyList());
    }


    
    @Test
    void createQuestion_ReturnsCreatedQuestion_WhenValid() throws Exception {
        CreateQuestionDTO dto = new CreateQuestionDTO();
        dto.setTitle("Título da Pergunta");
        dto.setDescription("Descrição da Pergunta");
        dto.setMaterialId(1L);

        when(questionService.createQuestion(any(CreateQuestionDTO.class), any(Authentication.class)))
                .thenReturn(testQuestion);

        mockMvc.perform(post("/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .principal(mockAuth))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testQuestion.getId()))
                .andExpect(jsonPath("$.title").value(testQuestion.getTitle()));

        verify(questionService, times(1)).createQuestion(any(CreateQuestionDTO.class), any(Authentication.class));
    }
    
    @Test
    void updateQuestion_ReturnsUpdatedQuestion_WhenValid() throws Exception {
        UpdateQuestionDTO dto = new UpdateQuestionDTO();
        dto.setTitle("Novo Título");
        dto.setDescription("Nova Descrição");

        Question updatedQuestion = new Question();
        updatedQuestion.setId(1L);
        updatedQuestion.setTitle(dto.getTitle());
        updatedQuestion.setDescription(dto.getDescription());
        updatedQuestion.setAuthor(testUser);

        when(questionService.updateQuestion(eq(1L), any(UpdateQuestionDTO.class), any(Authentication.class)))
                .thenReturn(updatedQuestion);

        mockMvc.perform(put("/questions/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .principal(mockAuth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedQuestion.getId()))
                .andExpect(jsonPath("$.title").value(dto.getTitle()));

        verify(questionService, times(1)).updateQuestion(eq(1L), any(UpdateQuestionDTO.class),
                any(Authentication.class));
    }


    
    @Test
    void deleteQuestion_ReturnsNoContent_WhenSuccessful() throws Exception {
        doNothing().when(questionService).deleteQuestion(eq(1L), any(Authentication.class));

        mockMvc.perform(delete("/questions/1")
                .principal(mockAuth))
                .andExpect(status().isNoContent());

        verify(questionService, times(1)).deleteQuestion(eq(1L), any(Authentication.class));
    }

    @Test
    void getQuestionsByMaterial_ReturnsListOfQuestions_WhenMaterialExists() throws Exception {
        QuestionResponseDTO responseDTO = new QuestionResponseDTO(
                1L,
                "Dúvida sobre o livro de física",
                "Onde posso encontrar mais exercícios sobre o capítulo 5?",
                "João da Silva",
                1L,
                LocalDateTime.now());
        List<QuestionResponseDTO> questions = Collections.singletonList(responseDTO);

        when(questionService.getQuestionsByMaterial(1L)).thenReturn(questions);

        mockMvc.perform(get("/questions/material/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Dúvida sobre o livro de física"));

        verify(questionService, times(1)).getQuestionsByMaterial(1L);
    }
}