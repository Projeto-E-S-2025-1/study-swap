package com.studyswap.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "materials")
public class Material {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "O título é obrigatório")
    private String title;

    // Descrição é opcional
    @Column(nullable = true, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "O tipo do material é obrigatório")
    private MaterialType materialType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "O estado de conservação é obrigatório")
    private ConservationStatus conservationStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "O tipo de transação é obrigatório")
    private TransactionType transactionType;

    // É null em casos de doação e troca
    @Column(nullable = true)
    @Positive(message = "O preço deve ser maior que zero")
    private Double price;

    // A foto é opcional
    @Column(nullable = true)
    private String photo;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "O usuário é obrigatório")
    private User user;
    
    private boolean available=true;
    
    public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public Material() {
    }

    public Material(String title, String description, MaterialType materialType,
            ConservationStatus conservationStatus, TransactionType transactionType,
            Double price, String photo, User user) {
        this.title = title;
        this.description = description;
        this.materialType = materialType;
        this.conservationStatus = conservationStatus;
        this.transactionType = transactionType;
        this.price = price;
        this.photo = photo;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
