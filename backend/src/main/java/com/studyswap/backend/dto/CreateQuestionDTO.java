package com.studyswap.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateQuestionDTO {

    @NotBlank(message = "O título é obrigatório")
    private String title;

    @NotBlank(message = "A descrição é obrigatória")
    private String description;

    @NotNull(message = "O ID do material é obrigatório")
    private Long materialId;

    public CreateQuestionDTO() {
    }

    public CreateQuestionDTO(String title, String description, Long materialId) {
        this.title = title;
        this.description = description;
        this.materialId = materialId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }
}
