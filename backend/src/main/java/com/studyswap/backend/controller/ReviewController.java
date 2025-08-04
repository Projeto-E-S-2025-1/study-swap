package com.studyswap.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.security.core.Authentication;
import jakarta.validation.Valid;

import com.studyswap.backend.dto.ReviewResponseDTO;
import com.studyswap.backend.dto.ReviewRequestDTO;
import com.studyswap.backend.model.Review;
import com.studyswap.backend.service.ReviewService;

import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponseDTO> createReview(@RequestBody @Valid ReviewRequestDTO dto, Authentication auth) {
        Review created = reviewService.createReview(dto, auth);
        ReviewResponseDTO response = new ReviewResponseDTO();
        response.setId(created.getId());
        response.setRating(created.getRating());
        response.setUserId(created.getAuthor().getId());
        response.setDescription(created.getDescription());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid ReviewRequestDTO dto,
            Authentication auth) {
        Review updated = reviewService.updateReview(id, dto, auth);
        ReviewResponseDTO response = new ReviewResponseDTO();
        response.setId(updated.getId());
        response.setRating(updated.getRating());
        response.setUserId(updated.getAuthor().getId());
        response.setDescription(updated.getDescription());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication auth) {
        reviewService.deleteReview(id, auth);
        return ResponseEntity.noContent().build();
    }
}
