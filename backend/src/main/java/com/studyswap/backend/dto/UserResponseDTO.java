package com.studyswap.backend.dto;

import com.studyswap.backend.model.Role;

public class UserResponseDTO {
    private Long id;
    private String name;
    private String photoUrl;
    private String interests;
    private Role role;

    public UserResponseDTO() {}

    public UserResponseDTO(Long id, String name, String photoUrl, String interests, Role role) {
        this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
        this.interests = interests;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
