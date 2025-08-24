package com.studyswap.api;


import com.studyswap.backend.controller.MaterialController;
import com.studyswap.backend.model.User;
import com.studyswap.backend.service.AuthService;
import com.studyswap.backend.service.MaterialService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MaterialControllerDeleteTest {

    @Mock
    private AuthService authService;

    @Mock
    private MaterialService materialService;

    @InjectMocks
    private MaterialController materialController;

    private User testUser;
    private User anotherUser;

    @BeforeEach
    void setUp() {
        // Prepara um usuário que é o dono do material
        testUser = new User("Test User", "test@example.com", "password", null);
        testUser.setId(1L);

        // Prepara um segundo usuário sem permissão
        anotherUser = new User("Another User", "another@example.com", "password", null);
        anotherUser.setId(2L);
    }

    @Test
    void testDeleteMaterial_Success() {
        // Define o comportamento do stub do AuthService para retornar o usuário logado
        when(authService.getAuthenticatedUser()).thenReturn(testUser);

        // O stub do MaterialService não precisa fazer nada, pois o método é void.
        // O Mockito vai apenas registrar a chamada.

        // 1. Executa o método que queremos testar
        ResponseEntity<String> response = materialController.deleteMaterial(1L);

        // 2. Verifica se o resultado está correto
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Material deletado com sucesso!", response.getBody());

        // 3. Verifica se o método de exclusão do serviço foi chamado
        verify(materialService, times(1)).deleteMaterial(eq(1L), eq(testUser));
    }

    @Test
    void testDeleteMaterial_NotFound() {
        // Define o comportamento do stub para simular material não encontrado
        when(authService.getAuthenticatedUser()).thenReturn(testUser);
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Material não encontrado"))
            .when(materialService).deleteMaterial(eq(99L), any(User.class));

        // 1. Executa e verifica se a exceção esperada é lançada
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            materialController.deleteMaterial(99L);
        });

        // 2. Verifica se a exceção tem o status e a mensagem corretos
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("404 NOT_FOUND \"Material não encontrado\"", exception.getMessage());
    }

    @Test
    void testDeleteMaterial_Forbidden() {
        // Define o comportamento do stub para simular falta de permissão
        when(authService.getAuthenticatedUser()).thenReturn(anotherUser);
        doThrow(new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para remover este material"))
            .when(materialService).deleteMaterial(eq(1L), any(User.class));

        // 1. Executa e verifica se a exceção esperada é lançada
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            materialController.deleteMaterial(1L);
        });

        // 2. Verifica se a exceção tem o status e a mensagem corretos
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("403 FORBIDDEN \"Você não tem permissão para remover este material\"", exception.getMessage());
    }
}