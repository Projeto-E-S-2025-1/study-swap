package com.studyswap.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.security.core.Authentication;
import jakarta.validation.Valid;

import com.studyswap.backend.dto.ReviewResponseDTO;
import com.studyswap.backend.dto.UserReviewAverageDTO;
import com.studyswap.backend.dto.ReviewRequestDTO;
import com.studyswap.backend.service.ReviewService;

import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> createReview(@RequestBody @Valid ReviewRequestDTO dto, Authentication auth) {
        ReviewResponseDTO created = reviewService.createReview(dto, auth);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid ReviewRequestDTO dto,
            Authentication auth) {
        ReviewResponseDTO updated = reviewService.updateReview(id, dto, auth);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication auth) {
        reviewService.deleteReview(id, auth);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<ReviewResponseDTO> getByTransactionId(@PathVariable Long transactionId) {
        ReviewResponseDTO dto = reviewService.getByTransactionId(transactionId);

        if (dto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponseDTO>> getByUser(@PathVariable Long userId){
        return ResponseEntity.ok().body(reviewService.getByUser(userId));
    }

    @GetMapping("/user/{userId}/average")
    public ResponseEntity<UserReviewAverageDTO> getUserAverageReview(@PathVariable Long userId) {
        UserReviewAverageDTO response = reviewService.getUserAverageRating(userId);
        return ResponseEntity.ok(response);
    }
}
