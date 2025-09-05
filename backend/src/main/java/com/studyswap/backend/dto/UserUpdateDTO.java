package com.studyswap.backend.dto;

public class UserUpdateDTO {
    private String name;
    private String interests;

    public UserUpdateDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }
}
