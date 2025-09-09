package com.studyswap.backend.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyswap.backend.controller.ReviewController;
import com.studyswap.backend.dto.ReviewRequestDTO;
import com.studyswap.backend.dto.ReviewResponseDTO;
import com.studyswap.backend.dto.UserReviewAverageDTO;
import com.studyswap.backend.repository.UserRepository;
import com.studyswap.backend.security.TokenService;
import com.studyswap.backend.service.AuthService;
import com.studyswap.backend.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("removal")
@MockBean
    private ReviewService reviewService;

    @SuppressWarnings("removal")
@MockBean
    private AuthService authService;

    @SuppressWarnings("removal")
@MockBean
    private TokenService tokenService;

    @SuppressWarnings("removal")
@MockBean
    private UserRepository userRepository;

    private ReviewRequestDTO reviewRequestDTO;
    private ReviewResponseDTO reviewResponseDTO;

    // Usuário simulado para testes
    private UserRequestPostProcessor testUser;

    @BeforeEach
    void setUp() {
        // Criando usuário simulado para MockMvc com Spring Security
        testUser = user("test@email.com").roles("STUDENT");

        reviewRequestDTO = new ReviewRequestDTO();
        reviewRequestDTO.setRating(4);
        reviewRequestDTO.setDescription("Ótimo material!");
        reviewRequestDTO.setAuthorId(1L);
        reviewRequestDTO.setTransactionId(1L);

        reviewResponseDTO = new ReviewResponseDTO(1L, "Test User", 1L, "Material Teste", 4,
                "Ótimo material!", LocalDateTime.now());
    }

    @Test
    @DisplayName("Deve criar uma avaliação e retornar 201 Created")
    void shouldCreateReviewAndReturn201() throws Exception {
        when(reviewService.createReview(any(ReviewRequestDTO.class), any())).thenReturn(reviewResponseDTO);

        mockMvc.perform(post("/review/{id}", 1)
                        .with(testUser)
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
        when(reviewService.getUserAverageRating(anyLong()))
                .thenReturn(new UserReviewAverageDTO(1L, 4.5, 2L));

        mockMvc.perform(get("/review/user/{userId}/average", 1L)
                        .with(testUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.averageRating").value(4.5))
                .andExpect(jsonPath("$.totalReviews").value(2L));
    }

    @Test
    @DisplayName("Deve retornar uma lista de avaliações do usuário")
    void shouldReturnReviewsByUser() throws Exception {
        when(reviewService.getByUser(anyLong()))
                .thenReturn(Collections.singletonList(reviewResponseDTO));

        mockMvc.perform(get("/review/user/{userId}", 1L)
                        .with(testUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].authorName").value("Test User"));
    }

    @Test
    @DisplayName("Deve retornar 404 se não houver review para a transação")
    void shouldReturn404IfReviewNotFound() throws Exception {
        when(reviewService.getByTransactionId(1L)).thenReturn(null);

        mockMvc.perform(get("/review/{transactionId}", 1L)
                        .with(testUser))
                .andExpect(status().isNotFound());

        verify(reviewService, times(1)).getByTransactionId(1L);
    }

    @Test
    @DisplayName("Deve retornar 404 ao atualizar review inexistente")
    void shouldReturn404WhenUpdatingNonExistentReview() throws Exception {
        when(reviewService.updateReview(eq(1L), any(ReviewRequestDTO.class), any()))
                .thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND));

        mockMvc.perform(put("/review/{id}", 1)
                        .with(testUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 404 ao tentar deletar review inexistente")
    void shouldReturn404WhenDeletingNonExistentReview() throws Exception {
        doThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND))
                .when(reviewService).deleteReview(eq(1L), any());

        mockMvc.perform(delete("/review/{id}", 1)
                        .with(testUser))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 403 se usuário não for o autor ao deletar review")
    void shouldReturn403WhenDeletingReviewWithoutPermission() throws Exception {
        doThrow(new ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN))
                .when(reviewService).deleteReview(eq(1L), any());

        mockMvc.perform(delete("/review/{id}", 1)
                        .with(testUser))
                .andExpect(status().isForbidden());
    }
}
