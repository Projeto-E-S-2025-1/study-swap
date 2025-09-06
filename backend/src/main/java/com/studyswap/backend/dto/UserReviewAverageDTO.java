package com.studyswap.backend.dto;

public class UserReviewAverageDTO {
    private Long userId;
    private Double averageRating;
    private Long totalReviews;

    public UserReviewAverageDTO(Long userId, Double averageRating, Long totalReviews) {
        this.userId = userId;
        this.averageRating = averageRating;
        this.totalReviews = totalReviews;
    }

    public Long getUserId() {
        return userId;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public Long getTotalReviews() {
        return totalReviews;
    }
}
