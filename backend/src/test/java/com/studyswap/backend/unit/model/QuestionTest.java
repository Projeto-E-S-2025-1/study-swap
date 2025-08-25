package com.studyswap.backend.unit.model;

import com.studyswap.backend.model.Material;
import com.studyswap.backend.model.Question;
import com.studyswap.backend.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class QuestionTest {

    @Test
    void testGettersAndSetters() {
        User user = new User();
        user.setId(1L);
        user.setName("usuarioTeste");

        Material material = new Material();
        material.setId(10L);
        material.setTitle("Livro Java");

        Question question = new Question(
                100L,
                "Descrição da pergunta",
                "Título da pergunta",
                user,
                material
        );

        assertEquals(100L, question.getId());
        assertEquals("Descrição da pergunta", question.getDescription());
        assertEquals("Título da pergunta", question.getTitle());
        assertEquals(user, question.getAuthor());
        assertEquals(material, question.getMaterial());

        question.setId(101L);
        question.setDescription("Nova descrição");
        question.setTitle("Novo título");
        question.setAuthor(new User());
        question.setMaterial(new Material());
        LocalDateTime now = LocalDateTime.now();
        question.setCreatedAt(now);

        assertEquals(101L, question.getId());
        assertEquals("Nova descrição", question.getDescription());
        assertEquals("Novo título", question.getTitle());
        assertNotNull(question.getAuthor());
        assertNotNull(question.getMaterial());
        assertEquals(now, question.getCreatedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        Question q1 = new Question();
        Question q2 = new Question();

        // Mesmo objeto (this == obj)
        assertEquals(q1, q1);

        // Comparar com null
        assertNotEquals(q1, null);

        // Comparar com objeto de outra classe
        assertNotEquals(q1, "string qualquer");

        // Ambos IDs nulos → equals deve ser true
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
