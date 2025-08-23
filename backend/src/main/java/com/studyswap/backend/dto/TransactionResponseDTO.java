package com.studyswap.backend.dto;

import java.time.LocalDateTime;

import com.studyswap.backend.model.Material;
import com.studyswap.backend.model.TransactionStatus;
import com.studyswap.backend.model.User;

public class TransactionResponseDTO {
    private Long id;
    private LocalDateTime transactionDate;
    private User receiver;
    private User announcer;
    private Material material;
    private TransactionStatus status;

    public TransactionResponseDTO(Long id, LocalDateTime transactionDate, User receiver,
    		User announcer, Material material, TransactionStatus status) {
        this.id = id;
        this.transactionDate = transactionDate;
        this.receiver = receiver;
        this.announcer = announcer;
        this.material= material; 
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public User getAnnouncer() {
        return announcer;
    }
    public void setAnnouncer(User announcer) {
        this.announcer = announcer;
    }
    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

}