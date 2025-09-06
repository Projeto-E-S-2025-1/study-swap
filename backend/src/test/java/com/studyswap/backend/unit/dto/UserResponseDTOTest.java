package com.studyswap.backend.unit.dto;

import com.studyswap.backend.dto.UserResponseDTO;
import com.studyswap.backend.model.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserResponseDTOTest {

    @Test
    void testNoArgsConstructorAndSettersGetters() {
        UserResponseDTO dto = new UserResponseDTO();

        assertNull(dto.getId());
        assertNull(dto.getName());
        assertNull(dto.getPhotoUrl());
        assertNull(dto.getInterests());
        assertNull(dto.getRole());

        dto.setId(1L);
        dto.setName("Matheus");
        dto.setPhotoUrl("/images/profile.png");
        dto.setInterests("Programação, IA");
        dto.setRole(Role.STUDENT);

        assertEquals(1L, dto.getId());
        assertEquals("Matheus", dto.getName());
        assertEquals("/images/profile.png", dto.getPhotoUrl());
        assertEquals("Programação, IA", dto.getInterests());
        assertEquals(Role.STUDENT, dto.getRole());
    }

    @Test
    void testAllArgsConstructorAndGetters() {
        UserResponseDTO dto = new UserResponseDTO(
                2L,
                "Sandra",
                "/images/default.png",
                "Leitura, Música",
                Role.STUDENT
        );

        assertEquals(2L, dto.getId());
        assertEquals("Sandra", dto.getName());
        assertEquals("/images/default.png", dto.getPhotoUrl());
        assertEquals("Leitura, Música", dto.getInterests());
        assertEquals(Role.STUDENT, dto.getRole());
    }
}
