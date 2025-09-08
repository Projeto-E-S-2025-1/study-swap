package com.studyswap.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReviewRequestDTO {

    @NotNull(message = "A nota é obrigatória")
    @Min(value = 1, message = "A nota mínima é 1")
    @Max(value = 5, message = "A nota máxima é 5")
    private Integer rating;

    @NotBlank(message = "O comentário é obrigatório")
    private String description;

    @NotNull(message = "Usuário não identificado")
    private Long authorId;

    @NotNull(message = "Transação não identificado")
    private Long transactionId;

    public ReviewRequestDTO() {
    }


    public ReviewRequestDTO(Long authorId, Long transactionId, int rating, String description) {
        this.authorId = authorId;
        this.transactionId = transactionId;
        this.rating = rating;
        this.description = description;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }    
}
