package com.studyswap.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reporter_id", nullable = false)
    @NotNull(message = "O criador da denúncia é obrigatório")
    private User reporter;

    @ManyToOne
    @JoinColumn(name = "reported_user_id")
    private User reportedUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_material_id")
    private Material reportedMaterial;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "O motivo da denúncia é obrigatório")
    private ReportReason reason;

    @Column(columnDefinition = "TEXT")
    private String description;

    public Report() {
    }

    public Report(User reporter, User reportedUser, Material reportedMaterial, 
            ReportReason reason, String description) {
        this.reporter = reporter;
        this.reportedUser = reportedUser;
        this.reportedMaterial = reportedMaterial;
        this.reason = reason;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getReporter() {
        return reporter;
    }

    public void setReporter(User reporter) {
        this.reporter = reporter;
    }

    public User getReportedUser() {
        return reportedUser;
    }

    public void setReportedUser(User reportedUser) {
        this.reportedUser = reportedUser;
    }

    public Material getReportedMaterial() {
        return reportedMaterial;
    }

    public void setReportedMaterial(Material reportedMaterial) {
        this.reportedMaterial = reportedMaterial;
    }

    public ReportReason getReason() {
        return reason;
    }

    public void setReason(ReportReason reason) {
        this.reason = reason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
