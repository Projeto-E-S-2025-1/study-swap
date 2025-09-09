package com.studyswap.backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotNull;

@Entity
public class Transaction {
    @ManyToOne
    @JoinColumn(name="material_id", nullable=false)
    private Material material;

    @ManyToOne
    @JoinColumn(name="announcer_id", nullable=false)
    private User announcer;

    @ManyToOne
    @JoinColumn(name="receiver_id", nullable=false)
    private User receiver;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(nullable = false, updatable=false)
    private LocalDateTime transactionDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "O Status da transação é obrigatório")
    private TransactionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "O Tipo da transação é obrigatório")
    private TransactionType type;   

    @JoinColumn(name="material_trade_id", nullable=true)
    @OneToOne(cascade = CascadeType.PERSIST)
    private Material materialTrade;
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	@PrePersist
    protected void onCreate() {
        this.transactionDate = LocalDateTime.now();
    }

    public Transaction() {
    }

    public Transaction(Material material, User announcer, User receiver, TransactionStatus status, TransactionType type, Material materialTrade) {
        this.material = material;
        this.announcer = announcer;
        this.receiver = receiver;
        this.status=status;
        this.type = type;
        this.materialTrade = materialTrade;
    }

    public Transaction(Material material, User announcer, User receiver, TransactionStatus status, TransactionType type) {
        this.material = material;
        this.announcer = announcer;
        this.receiver = receiver;
        this.status=status;
        this.type = type;
        this.materialTrade = null;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
    public User getAnnouncer() {
        return announcer;
    }
    public void setAnnouncer(User announcer) {
        this.announcer = announcer;
    }
    public User getReceiver() {
        return receiver;
    }
    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }
    public TransactionType getType() {
        return type;
    }
    public void setType(TransactionType type) {
        this.type = type;
    }
    public Material getMaterialTrade() {
        return materialTrade;
    }
    public void setMaterialTrade(Material materialTrade) {
        this.materialTrade = materialTrade;
    }
    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }
    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
}