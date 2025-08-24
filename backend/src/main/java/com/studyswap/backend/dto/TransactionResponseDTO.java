package com.studyswap.backend.dto;

import java.time.LocalDateTime;

import com.studyswap.backend.model.TransactionStatus;

public class TransactionResponseDTO {
    private Long id;
    private LocalDateTime transactionDate;

    private Long idMaterial;
    private TransactionStatus status;
    private Long receiverId;
    private String receiverName;
    private Long announcerId;
    private String announcerName;
    
    
    public TransactionResponseDTO(Long id, LocalDateTime transactionDate, Long idMaterial, TransactionStatus status,
			Long receiverId, String receiverName, Long announcerId, String announcerName) {
		this.id = id;
		this.transactionDate = transactionDate;
		this.idMaterial = idMaterial;
		this.status = status;
		this.receiverId = receiverId;
		this.receiverName = receiverName;
		this.announcerId = announcerId;
		this.announcerName = announcerName;
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

    public Long getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public Long getAnnouncerId() {
		return announcerId;
	}

	public void setAnnouncerId(Long announcerId) {
		this.announcerId = announcerId;
	}

	public String getAnnouncerName() {
		return announcerName;
	}

	public void setAnnouncerName(String announcerName) {
		this.announcerName = announcerName;
	}

	public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

	public Long getIdMaterial() {
		return idMaterial;
	}
	public void setIdMaterial(Long idMaterial) {
		this.idMaterial = idMaterial;
	}
}