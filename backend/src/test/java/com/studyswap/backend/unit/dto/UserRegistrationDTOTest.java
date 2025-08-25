package com.studyswap.backend.unit.dto;

import com.studyswap.backend.dto.UserRegistrationDTO;
import com.studyswap.backend.model.Role;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserRegistrationDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidDTO() {
        UserRegistrationDTO dto = new UserRegistrationDTO(
                "Nome Teste",
                "teste@email.com",
                "senha123",
                Role.STUDENT
        );

        Set<ConstraintViolation<UserRegistrationDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Não deveria haver violações para DTO válido");
    }

    @Test
    void testBlankName() {
        UserRegistrationDTO dto = new UserRegistrationDTO(
                "   ",
                "teste@email.com",
                "senha123",
                Role.STUDENT
        );

        Set<ConstraintViolation<UserRegistrationDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("O nome é obrigatório")));
    }

    @Test
    void testBlankEmail() {
        UserRegistrationDTO dto = new UserRegistrationDTO(
                "Nome Teste",
                "   ",
                "senha123",
                Role.STUDENT
        );

        Set<ConstraintViolation<UserRegistrationDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("O email é obrigatório")));
    }

    @Test
    void testInvalidEmail() {
        UserRegistrationDTO dto = new UserRegistrationDTO(
                "Nome Teste",
                "email-invalido",
                "senha123",
                Role.STUDENT
        );

        Set<ConstraintViolation<UserRegistrationDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("O email deve ser válido")));
    }

    @Test
    void testBlankPassword() {
        UserRegistrationDTO dto = new UserRegistrationDTO(
                "Nome Teste",
                "teste@email.com",
                " ",
                Role.STUDENT
        );

        Set<ConstraintViolation<UserRegistrationDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("A senha é obrigatória")));
    }

    @Test
    void testNullRole() {
        UserRegistrationDTO dto = new UserRegistrationDTO(
                "Nome Teste",
                "teste@email.com",
                "senha123",
                null
        );

        Set<ConstraintViolation<UserRegistrationDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Role é obrigatório")));
    }

    @Test
    void testSettersAndGetters() {
        UserRegistrationDTO dto = new UserRegistrationDTO();

        dto.setName("Novo Nome");
        dto.setEmail("novo@email.com");
        dto.setPassword("novaSenha");
        dto.setRole(Role.STUDENT);

        assertEquals("Novo Nome", dto.getName());
        assertEquals("novo@email.com", dto.getEmail());
        assertEquals("novaSenha", dto.getPassword());
        assertEquals(Role.STUDENT, dto.getRole());
    }
}
