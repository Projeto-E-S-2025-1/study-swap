package com.studyswap.backend.model;

public enum Role {
    STUDENT("STUDENT");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
