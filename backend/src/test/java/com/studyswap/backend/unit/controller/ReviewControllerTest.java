package com.studyswap.backend.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyswap.backend.controller.ReviewController;
import com.studyswap.backend.dto.ReviewRequestDTO;
import com.studyswap.backend.dto.ReviewResponseDTO;
import com.studyswap.backend.dto.UserReviewAverageDTO;
import com.studyswap.backend.model.User;
import com.studyswap.backend.repository.UserRepository; // Adicione esta importação
import com.studyswap.backend.security.TokenService;
import com.studyswap.backend.service.AuthService;
import com.studyswap.backend.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ReviewController.class)
public class ReviewControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @SuppressWarnings("removal")
        @MockBean
        private ReviewService reviewService;

        // Dependências de segurança e outras classes necessárias para o contexto de
        // teste
        @SuppressWarnings("removal")
        @MockBean
        private AuthService authService;
        @SuppressWarnings("removal")
        @MockBean
        private TokenService tokenService;

        // Adicione esta linha
        @SuppressWarnings("removal")
        @MockBean
        private UserRepository userRepository;

        private User user;
        private ReviewRequestDTO reviewRequestDTO;
        private ReviewResponseDTO reviewResponseDTO;

           @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@email.com");
        // CORREÇÃO: Defina o papel (role) do usuário
        user.setRole(com.studyswap.backend.model.Role.STUDENT); 

        reviewRequestDTO = new ReviewRequestDTO();
        reviewRequestDTO.setRating(4);
        reviewRequestDTO.setDescription("Ótimo material!");
        reviewRequestDTO.setAuthorId(1L);
        reviewRequestDTO.setTransactionId(1L);

        reviewResponseDTO = new ReviewResponseDTO(1L, "Test User", 1L, "Material Teste", 4, "Ótimo material!",
                LocalDateTime.now());
    }

        // Seus testes...

        @Test
        @DisplayName("Deve criar uma avaliação e retornar 201 Created")
        void shouldCreateReviewAndReturn201() throws Exception {
                when(reviewService.createReview(any(ReviewRequestDTO.class), any())).thenReturn(reviewResponseDTO);

                mockMvc.perform(post("/review/{id}", 1)
                                .with(user(user))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(reviewRequestDTO)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(reviewResponseDTO.getId()))
                                .andExpect(jsonPath("$.rating").value(reviewResponseDTO.getRating()))
                                .andExpect(jsonPath("$.description").value(reviewResponseDTO.getDescription()));

                verify(reviewService, times(1)).createReview(any(ReviewRequestDTO.class), any());
        }

        @Test
        @DisplayName("Deve retornar a média de avaliações do usuário")
        void shouldReturnUserAverageReview() throws Exception {
                // Arrange
                when(reviewService.getUserAverageRating(ArgumentMatchers.anyLong()))
                                .thenReturn(new UserReviewAverageDTO(1L, 4.5, 2L));

                // Act & Assert
                mockMvc.perform(get("/review/user/{userId}/average", 1L)
                                .with(user(user)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.userId").value(1L))
                                .andExpect(jsonPath("$.averageRating").value(4.5))
                                .andExpect(jsonPath("$.totalReviews").value(2L));
        }

        @Test
        @DisplayName("Deve retornar uma lista de avaliações do usuário")
        void shouldReturnReviewsByUser() throws Exception {
                // Arrange
                when(reviewService.getByUser(ArgumentMatchers.anyLong()))
                                .thenReturn(Collections.singletonList(reviewResponseDTO));

                // Act & Assert
                mockMvc.perform(get("/review/user/{userId}", 1L)
                                .with(user(user)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].authorName").value("Test User"));
        }
}