package com.studyswap.backend.dto;

import java.time.LocalDateTime;

public class ReviewResponseDTO {
    private Long id;
    private String authorName;
    private Long authorId;
    private String materialTitle;
    private Integer rating;
    private String description;
    private LocalDateTime createdAt;

    public ReviewResponseDTO(){

    }

    public ReviewResponseDTO(Long id, String authorName, Long authorId,  String materialTitle, Integer rating, String description,
            LocalDateTime createdAt) {
        this.id = id;
        this.authorName = authorName;
        this.authorId = authorId;
        this.materialTitle = materialTitle;
        this.rating = rating;
        this.description = description;
        this.createdAt = createdAt;
    }
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Integer getRating() {
        return rating;
    }
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    public String getAuthorName() {
        return authorName;
    }
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    public String getMaterialTitle() {
        return materialTitle;
    }
    public void setMaterialTitle(String materialTitle) {
        this.materialTitle = materialTitle;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public Long getAuthorId() {
        return authorId;
    }
    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
}
