package com.studyswap.backend.dto;

import com.studyswap.backend.model.ReportReason;

import jakarta.validation.constraints.NotNull;

public class CreateReportDTO {

    private Long reportedUserId;

    private Long reportedMaterialId;

    @NotNull(message = "O motivo da denúncia é obrigatório")
    private ReportReason reason;

    private String description;

    public CreateReportDTO() {
    }

    public CreateReportDTO(Long reportedUserId, Long reportedMaterialId,
            @NotNull(message = "O motivo da denúncia é obrigatório") ReportReason reason, String description) {
        this.reportedUserId = reportedUserId;
        this.reportedMaterialId = reportedMaterialId;
        this.reason = reason;
        this.description = description;
    }

    public Long getReportedUserId() {
        return reportedUserId;
    }

    public void setReportedUserId(Long reportedUserId) {
        this.reportedUserId = reportedUserId;
    }

    public Long getReportedMaterialId() {
        return reportedMaterialId;
    }

    public void setReportedMaterialId(Long reportedMaterialId) {
        this.reportedMaterialId = reportedMaterialId;
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
