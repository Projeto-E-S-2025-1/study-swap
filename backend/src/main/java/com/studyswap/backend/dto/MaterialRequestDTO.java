package com.studyswap.backend.dto;

import com.studyswap.backend.model.ConservationStatus;
import com.studyswap.backend.model.MaterialType;
import com.studyswap.backend.model.TransactionType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class MaterialRequestDTO {
    @NotBlank(message = "O título é obrigatório")
    private String title;

    private String description;

    @NotNull(message = "O tipo do material é obrigatório")
    private MaterialType materialType;

    @NotNull(message = "O estado de conservação é obrigatório")
    private ConservationStatus conservationStatus;

    @NotNull(message = "O tipo de transação é obrigatório")
    private TransactionType transactionType;

    @Positive(message = "O preço deve ser maior que zero")
    private Double price;

    private String photo;

    public MaterialRequestDTO() {
    }

    public MaterialRequestDTO(String title, String description, MaterialType materialType,
            ConservationStatus conservationStatus, TransactionType transactionType,
            Double price, String photo) {
        this.title = title;
        this.description = description;
        this.materialType = materialType;
        this.conservationStatus = conservationStatus;
        this.transactionType = transactionType;
        this.price = price;
        this.photo = photo;
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

    public MaterialType getMaterialType() {
        return materialType;
    }

    public void setMaterialType(MaterialType materialType) {
        this.materialType = materialType;
    }

    public ConservationStatus getConservationStatus() {
        return conservationStatus;
    }

    public void setConservationStatus(ConservationStatus conservationStatus) {
        this.conservationStatus = conservationStatus;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
