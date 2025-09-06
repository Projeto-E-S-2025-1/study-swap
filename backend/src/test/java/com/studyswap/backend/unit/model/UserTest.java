package com.studyswap.backend.unit.model;

import com.studyswap.backend.model.Role;
import com.studyswap.backend.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testNoArgsConstructor() {
        User user = new User();
        assertNotNull(user);
    }

    @Test
    void testAllArgsConstructorAndGetters() {
        User user = new User("Matheus", "matheus@example.com", "123456", Role.STUDENT);

        assertEquals("Matheus", user.getName());
        assertEquals("matheus@example.com", user.getEmail());
        assertEquals("123456", user.getPassword());
        assertEquals(Role.STUDENT, user.getRole());
    }

    @Test
    void testSettersAndGetters() {
        User user = new User();
        user.setId(1L);
        user.setName("Ana");
        user.setEmail("ana@example.com");
        user.setPassword("senha123");
        user.setRole(Role.STUDENT);
        user.setPhotoUrl("/images/default-profile-photo.png");
        user.setInterests("Comprar materiais");

        assertEquals(1L, user.getId());
        assertEquals("Ana", user.getName());
        assertEquals("ana@example.com", user.getEmail());
        assertEquals("senha123", user.getPassword());
        assertEquals(Role.STUDENT, user.getRole());
        assertEquals("/images/default-profile-photo.png", user.getPhotoUrl());
        assertEquals("Comprar materiais", user.getInterests());
    }

    @Test
    void testGetAuthorities() {
        User user = new User("João", "joao@example.com", "pass", Role.STUDENT);

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("STUDENT")));
    }

    @Test
    void testGetUsernameReturnsEmail() {
        User user = new User("Maria", "maria@example.com", "pass", Role.STUDENT);
        assertEquals("maria@example.com", user.getUsername());
    }
}
