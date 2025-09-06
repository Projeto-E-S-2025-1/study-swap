package com.studyswap.backend.unit.dto;

import org.junit.jupiter.api.Test;

import com.studyswap.backend.dto.UserUpdateDTO;

import static org.junit.jupiter.api.Assertions.*;

class UserUpdateDTOTest {

    @Test
    void testDefaultConstructorAndSettersGetters() {
        UserUpdateDTO dto = new UserUpdateDTO();

        assertNull(dto.getName());
        assertNull(dto.getInterests());

        dto.setName("Matheus");
        dto.setInterests("Comprar livros usados");

        assertEquals("Matheus", dto.getName());
        assertEquals("Comprar livros usados", dto.getInterests());
    }
}
