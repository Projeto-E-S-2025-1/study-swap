package com.studyswap.backend.unit.dto;

import com.studyswap.backend.dto.UserLoginDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserLoginDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidDTO() {
        UserLoginDTO dto = new UserLoginDTO("teste@email.com", "senha123");
        Set<ConstraintViolation<UserLoginDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Não deveria haver violações para DTO válido");
    }

    @Test
    void testBlankEmail() {
        UserLoginDTO dto = new UserLoginDTO("  ", "senha123");
        Set<ConstraintViolation<UserLoginDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("O email é obrigatório")));
    }

    @Test
    void testInvalidEmail() {
        UserLoginDTO dto = new UserLoginDTO("email-invalido", "senha123");
        Set<ConstraintViolation<UserLoginDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("O email deve ser válido")));
    }

    @Test
    void testBlankPassword() {
        UserLoginDTO dto = new UserLoginDTO("teste@email.com", " ");
        Set<ConstraintViolation<UserLoginDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("A senha é obrigatória")));
    }

    @Test
    void testSettersAndGetters() {
        UserLoginDTO dto = new UserLoginDTO();

        dto.setEmail("teste2@email.com");
        dto.setPassword("novaSenha");

        assertEquals("teste2@email.com", dto.getEmail());
        assertEquals("novaSenha", dto.getPassword());
    }
}
