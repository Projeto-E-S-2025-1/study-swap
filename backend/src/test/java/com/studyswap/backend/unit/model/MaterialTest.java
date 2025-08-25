package com.studyswap.backend.unit.model;

import com.studyswap.backend.model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MaterialTest {

    @Test
    void testGettersAndSetters() {
        // Mock User
        User user = new User();
        user.setId(1L);
        user.setEmail("teste@email.com");
        user.setName("usuarioTeste");

        // Criar Material usando construtor
        Material material = new Material(
                "Livro Java",
                "Livro sobre Java avançado",
                MaterialType.LIVRO,
                ConservationStatus.NOVO,
                TransactionType.VENDA,
                100.0,
                "foto.png",
                user
        );

        // Verificar construtor
        assertEquals("Livro Java", material.getTitle());
        assertEquals("Livro sobre Java avançado", material.getDescription());
        assertEquals(MaterialType.LIVRO, material.getMaterialType());
        assertEquals(ConservationStatus.NOVO, material.getConservationStatus());
        assertEquals(TransactionType.VENDA, material.getTransactionType());
        assertEquals(100.0, material.getPrice());
        assertEquals("foto.png", material.getPhoto());
        assertEquals(user, material.getUser());
        assertTrue(material.isAvailable());

        // Testar setters
        material.setId(42L);
        material.setTitle("Novo Título");
        material.setDescription("Nova descrição");
        material.setMaterialType(MaterialType.APOSTILA);
        material.setConservationStatus(ConservationStatus.RAZOAVEL);
        material.setTransactionType(TransactionType.DOACAO);
        material.setPrice(null);
        material.setPhoto("novaFoto.jpg");
        material.setAvailable(false);

        // Testar getters
        assertEquals(42L, material.getId());
        assertEquals("Novo Título", material.getTitle());
        assertEquals("Nova descrição", material.getDescription());
        assertEquals(MaterialType.APOSTILA, material.getMaterialType());
        assertEquals(ConservationStatus.RAZOAVEL, material.getConservationStatus());
        assertEquals(TransactionType.DOACAO, material.getTransactionType());
        assertNull(material.getPrice());
        assertEquals("novaFoto.jpg", material.getPhoto());
        assertFalse(material.isAvailable());
    }

    @Test
    void testDefaultConstructor() {
        Material material = new Material();
        // Valor default do booleano
        assertTrue(material.isAvailable());
    }
}
