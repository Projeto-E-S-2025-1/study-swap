package com.studyswap.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyswap.backend.BackendApplication; // Importe a classe principal da aplicação
import com.studyswap.backend.controller.ReviewController;
import com.studyswap.backend.dto.ReviewRequestDTO;
import com.studyswap.backend.model.Review;
import com.studyswap.backend.model.User;
import com.studyswap.backend.service.ReviewService;
import com.studyswap.backend.BackendApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean; // Use @MockBean para injetar o mock
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ReviewController.class, value = BackendApplication.class) // Adicione 'classes =
                                                                                      // BackendApplication.class'
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean // Use @MockBean para o Spring gerenciar o mock
    private ReviewService reviewService;

    private ReviewRequestDTO reviewRequestDTO;
    private Review mockReview;
    private User mockUser;

    @BeforeEach
    void setUp() {
        reviewRequestDTO = new ReviewRequestDTO();
        reviewRequestDTO.setRating(5);
        reviewRequestDTO.setDescription("Ótimo material!");
        reviewRequestDTO.setTransactonId(1L);

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setName("Teste User");

        mockReview = new Review();
        mockReview.setId(1L);
        mockReview.setRating(5);
        mockReview.setDescription("Ótimo material!");
        mockReview.setAuthor(mockUser);
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void shouldCreateReviewSuccessfully() throws Exception {
        when(reviewService.createReview(any(ReviewRequestDTO.class), any(Authentication.class))).thenReturn(mockReview);

        mockMvc.perform(post("/review")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(mockReview.getId()))
                .andExpect(jsonPath("$.rating").value(mockReview.getRating()))
                .andExpect(jsonPath("$.description").value(mockReview.getDescription()))
                .andExpect(jsonPath("$.userId").value(mockUser.getId()));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void shouldFailToCreateReviewWhenTransactionAlreadyRated() throws Exception {
        doThrow(new IllegalStateException("Essa transacao ja foi avaliada.")).when(reviewService)
                .createReview(any(ReviewRequestDTO.class), any(Authentication.class));

        mockMvc.perform(post("/review")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Essa transacao ja foi avaliada."));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void shouldUpdateReviewSuccessfully() throws Exception {
        ReviewRequestDTO updatedDto = new ReviewRequestDTO();
        updatedDto.setRating(4);
        updatedDto.setDescription("Material bom, mas poderia ser melhor.");
        updatedDto.setTransactonId(1L);

        Review updatedReview = new Review();
        updatedReview.setId(1L);
        updatedReview.setRating(4);
        updatedReview.setDescription("Material bom, mas poderia ser melhor.");
        updatedReview.setAuthor(mockUser);

        when(reviewService.updateReview(anyLong(), any(ReviewRequestDTO.class), any(Authentication.class)))
                .thenReturn(updatedReview);

        mockMvc.perform(put("/review/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedReview.getId()))
                .andExpect(jsonPath("$.rating").value(updatedReview.getRating()))
                .andExpect(jsonPath("$.description").value(updatedReview.getDescription()));
    }

    @Test
    @WithMockUser(username = "another@example.com")
    void shouldFailToUpdateReviewWithForbiddenUser() throws Exception {
        ReviewRequestDTO updatedDto = new ReviewRequestDTO();
        updatedDto.setRating(4);
        updatedDto.setDescription("Material bom, mas poderia ser melhor.");
        updatedDto.setTransactonId(1L);

        doThrow(new SecurityException("Voce nao tem permissao para editar esta avaliacao.")).when(reviewService)
                .updateReview(anyLong(), any(ReviewRequestDTO.class), any(Authentication.class));

        mockMvc.perform(put("/review/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Voce nao tem permissao para editar esta avaliacao."));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void shouldDeleteReviewSuccessfully() throws Exception {
        doNothing().when(reviewService).deleteReview(anyLong(), any(Authentication.class));

        mockMvc.perform(delete("/review/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "another@example.com")
    void shouldFailToDeleteReviewWithForbiddenUser() throws Exception {
        doThrow(new SecurityException("Voce nao tem permissao para excluir esta avaliacao.")).when(reviewService)
                .deleteReview(anyLong(), any(Authentication.class));

        mockMvc.perform(delete("/review/{id}", 1L))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Voce nao tem permissao para excluir esta avaliacao."));
    }
}