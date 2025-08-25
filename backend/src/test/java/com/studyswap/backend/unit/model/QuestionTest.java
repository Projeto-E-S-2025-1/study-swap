package com.studyswap.backend.unit.model;

import com.studyswap.backend.model.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class QuestionTest {

    @Test
    void testGettersAndSetters() {
        // Mock User e Material
        User user = new User();
        user.setId(1L);
        user.setName("usuarioTeste");

        Material material = new Material();
        material.setId(10L);
        material.setTitle("Livro Java");

        // Criar Question usando construtor completo
        Question question = new Question(
                100L,
                "Descrição da pergunta",
                "Título da pergunta",
                user,
                material
        );

        // Verificar construtor
        assertEquals(100L, question.getId());
        assertEquals("Descrição da pergunta", question.getDescription());
        assertEquals("Título da pergunta", question.getTitle());
        assertEquals(user, question.getAuthor());
        assertEquals(material, question.getMaterial());

        // Testar setters
        question.setId(101L);
        question.setDescription("Nova descrição");
        question.setTitle("Novo título");
        question.setAuthor(new User());
        question.setMaterial(new Material());
        LocalDateTime now = LocalDateTime.now();
        question.setCreatedAt(now);

        // Testar getters
        assertEquals(101L, question.getId());
        assertEquals("Nova descrição", question.getDescription());
        assertEquals("Novo título", question.getTitle());
        assertNotNull(question.getAuthor());
        assertNotNull(question.getMaterial());
        assertEquals(now, question.getCreatedAt());
    }

    @Test
    void testDefaultConstructorAndEqualsHashCode() {
        Question q1 = new Question();
        Question q2 = new Question();

        // IDs nulos
        assertEquals(q1, q2);  

        // IDs iguais
        q1.setId(1L);
        q2.setId(1L);
        assertEquals(q1, q2);
        assertEquals(q1.hashCode(), q2.hashCode());

        // IDs diferentes
        q2.setId(2L);
        assertNotEquals(q1, q2);
    }

}
