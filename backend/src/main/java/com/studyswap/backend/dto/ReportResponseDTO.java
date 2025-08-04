package com.studyswap.backend.dto;

import com.studyswap.backend.model.ReportReason;

public class ReportResponseDTO {

    private Long id;

    private Long reporterId;

    private Long reportedUserId;

    private Long reportedMaterialId;

    private ReportReason reason;

    private String description;
    
    public ReportResponseDTO() {
    }
    
    public ReportResponseDTO(Long id, Long reporterId, Long reportedUserId, Long reportedMaterialId,
            ReportReason reason, String description) {
        this.id = id;
        this.reporterId = reporterId;
        this.reportedUserId = reportedUserId;
        this.reportedMaterialId = reportedMaterialId;
        this.reason = reason;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReporterId() {
        return reporterId;
    }

    public void setReporterId(Long reporterId) {
        this.reporterId = reporterId;
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
