package com.studyswap.backend.unit.dto;

import com.studyswap.backend.dto.UserLoginDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

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

    static Stream<Arguments> invalidDTOs() {
        return Stream.of(
                Arguments.of(new UserLoginDTO("  ", "senha123"), "O email é obrigatório"),
                Arguments.of(new UserLoginDTO("email-invalido", "senha123"), "O email deve ser válido"),
                Arguments.of(new UserLoginDTO("teste@email.com", " "), "A senha é obrigatória")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidDTOs")
    void testInvalidDTOs(UserLoginDTO dto, String expectedMessage) {
        Set<ConstraintViolation<UserLoginDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty(), "Deveria haver violações para DTO inválido");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains(expectedMessage)));
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
