package com.studyswap.backend.unit.repository;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.studyswap.backend.model.Role;
import com.studyswap.backend.model.User;
import com.studyswap.backend.repository.UserRepository;

import java.util.Optional;

@DataJpaTest
@DisplayName("UserRepository Integration Tests")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Salvar um usuário")
    void testSaveUser() {
        // Cenário
        User user = new User("Test User", "test@example.com", "password", Role.STUDENT);

        // Ação
        User savedUser = userRepository.save(user);

        // Verificação
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Buscar usuário por ID")
    void testFindById() {
        // Cenário
        User user = new User("Jane Doe", "jane@example.com", "password", Role.STUDENT);
        User savedUser = userRepository.save(user);

        // Ação
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        // Verificação
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("Jane Doe");
    }

    @Test
    @DisplayName("Deletar usuário por ID")
    void testDeleteById() {
        // Cenário
        User user = new User("John Smith", "john@example.com", "password", Role.STUDENT);
        User savedUser = userRepository.save(user);

        // Ação
        userRepository.deleteById(savedUser.getId());
        Optional<User> deletedUser = userRepository.findById(savedUser.getId());

        // Verificação
        assertThat(deletedUser).isNotPresent();
    }
    
    @Test
    @DisplayName("Buscar usuário por e-mail")
    void testFindByEmail() {
        // Cenário
        User user = new User("User Email", "user.email@example.com", "password", Role.STUDENT);
        userRepository.save(user);

        // Ação
        Optional<User> foundUser = userRepository.findByEmail("user.email@example.com");

        // Verificação
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("User Email");
    }

    @Test
    @DisplayName("Não encontrar usuário por e-mail inexistente")
    void testFindByEmailNotFound() {
        // Cenário: Nenhum usuário com este e-mail existe
        String nonExistentEmail = "nonexistent@example.com";

        // Ação
        Optional<User> foundUser = userRepository.findByEmail(nonExistentEmail);

        // Verificação
        assertThat(foundUser).isNotPresent();
    }
}