// Novo DTO para resposta
package com.studyswap.backend.dto;

import com.studyswap.backend.model.ConservationStatus;
import com.studyswap.backend.model.MaterialType;
import com.studyswap.backend.model.TransactionType;

public class MaterialResponseDTO {
    private boolean available;
    private Long id;
    private String title;
    private String description;
    private MaterialType materialType;
    private ConservationStatus conservationStatus;
    private TransactionType transactionType;
    private Double price;
    private String photo;
    private Long userId;
    private String userName;

	public MaterialResponseDTO() {}

    public MaterialResponseDTO(Long id, String title, String description, MaterialType materialType,
            ConservationStatus conservationStatus, TransactionType transactionType,
            Double price, String photo, Long userId, String userName, boolean available) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.materialType = materialType;
        this.conservationStatus = conservationStatus;
        this.transactionType = transactionType;
        this.price = price;
        this.photo = photo;
        this.userId = userId;
        this.userName = userName;
        this.available=available;
    }
    public boolean isAvailable() {
    	return available;
    }
    
    public void setAvailable(boolean available) {
    	this.available = available;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
