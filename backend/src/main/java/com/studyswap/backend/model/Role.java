package com.studyswap.backend.model;

public enum Role {
    STUDENT("STUDENT");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRole() {
        return roleName;
    }
}
