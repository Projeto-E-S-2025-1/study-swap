package com.studyswap.backend.dto;

import com.studyswap.backend.model.ConservationStatus;
import com.studyswap.backend.model.MaterialType;

public class MaterialDTO {
    private Long id;
    private String title;
    private MaterialType type;
    private ConservationStatus conservationStatus;

    public MaterialDTO(Long id, String title, MaterialType type, ConservationStatus conservationStatus) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.conservationStatus = conservationStatus;
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
    public MaterialType getType() {
        return type;
    }
    public void setType(MaterialType type) {
        this.type = type;
    }
    public ConservationStatus getConservationStatus() {
        return conservationStatus;
    }
    public void setConservationStatus(ConservationStatus conservationStatus) {
        this.conservationStatus = conservationStatus;
    }
}
