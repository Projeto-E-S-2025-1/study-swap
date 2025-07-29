package com.studyswap.backend.dto;

import java.time.LocalDateTime;

public class QuestionResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String authorName;
    private LocalDateTime createdAt;

    public QuestionResponseDTO(Long id, String title, String description, String authorName, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.authorName = authorName;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
