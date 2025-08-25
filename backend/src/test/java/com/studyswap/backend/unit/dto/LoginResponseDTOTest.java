package com.studyswap.backend.unit.dto;

import com.studyswap.backend.dto.LoginResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginResponseDTOTest {

    @Test
    void testConstructorAndGetter() {
        LoginResponseDTO dto = new LoginResponseDTO("fake-jwt-token");

        assertEquals("fake-jwt-token", dto.getToken());
    }
}
