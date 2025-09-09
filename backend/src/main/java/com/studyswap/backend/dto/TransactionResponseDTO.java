package com.studyswap.backend.dto;

import java.time.LocalDateTime;

import com.studyswap.backend.model.TransactionStatus;
import com.studyswap.backend.model.TransactionType;

public class TransactionResponseDTO {
    private Long id;
    private LocalDateTime transactionDate;

    private Long idMaterial;
	private String materialName;
	private TransactionStatus status;
    private Long receiverId;
    private String receiverName;
    private Long announcerId;
    private String announcerName;
	private TransactionType transactionType;
	private MaterialDTO offeredMaterial;
    
    public TransactionResponseDTO(Long id, LocalDateTime transactionDate, Long idMaterial, String materialName, TransactionStatus status,
			Long receiverId, String receiverName, Long announcerId, String announcerName, TransactionType transactionType,
			MaterialDTO offeredMaterial) {
		this.id = id;
		this.transactionDate = transactionDate;
		this.idMaterial = idMaterial;
		this.materialName = materialName;
		this.status = status;
		this.receiverId = receiverId;
		this.receiverName = receiverName;
		this.announcerId = announcerId;
		this.announcerName = announcerName;
		this.transactionType = transactionType;
		this.offeredMaterial = offeredMaterial;
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

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public MaterialDTO getOfferedMaterial() {
		return offeredMaterial;
	}
	
	public void setOfferedMaterial(MaterialDTO offeredMaterial) {
		this.offeredMaterial = offeredMaterial;
	}
}