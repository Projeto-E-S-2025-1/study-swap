package com.studyswap.backend.unit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.studyswap.backend.model.ConservationStatus;
import com.studyswap.backend.model.Material;
import com.studyswap.backend.model.MaterialType;
import com.studyswap.backend.model.Question;
import com.studyswap.backend.model.Role;
import com.studyswap.backend.model.TransactionType;
import com.studyswap.backend.model.User;
import com.studyswap.backend.repository.QuestionRepository;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName("QuestionRepository Integration Tests")
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User testUser;
    private Material testMaterial;

    @BeforeEach
    void setUp() {
        // Setup de dados de teste antes de cada método de teste
        testUser = new User("Question Tester", "questiontest@example.com", "password", Role.STUDENT);
        entityManager.persist(testUser);

        testMaterial = new Material("Test Material", "For a test.", MaterialType.LIVRO,
                ConservationStatus.NOVO, TransactionType.VENDA, 15.0, "/images/test.png", testUser);
        entityManager.persist(testMaterial);
        
        entityManager.flush();
    }

    @Test
    @DisplayName("Salvar uma pergunta")
    void testSaveQuestion() {
        // Cenário
        Question question = new Question(null, "How to create a question?", "Creation", testUser, testMaterial);

        // Ação
        Question savedQuestion = questionRepository.save(question);

        // Verificação
        assertThat(savedQuestion).isNotNull();
        assertThat(savedQuestion.getId()).isNotNull();
        assertThat(savedQuestion.getDescription()).isEqualTo("How to create a question?");
    }

    @Test
    @DisplayName("Buscar pergunta por ID")
    void testFindById() {
        // Cenário
        Question question = new Question(null, "What is a repository?", "Repository", testUser, testMaterial);
        Question savedQuestion = entityManager.persist(question);
        entityManager.flush();

        // Ação
        Optional<Question> foundQuestion = questionRepository.findById(savedQuestion.getId());

        // Verificação
        assertThat(foundQuestion).isPresent();
        assertThat(foundQuestion.get().getTitle()).isEqualTo("Repository");
    }

    @Test
    @DisplayName("Deletar pergunta por ID")
    void testDeleteById() {
        // Cenário
        Question question = new Question(null, "Delete this question.", "Deletion", testUser, testMaterial);
        Question savedQuestion = entityManager.persist(question);
        entityManager.flush();

        // Ação
        questionRepository.deleteById(savedQuestion.getId());
        Optional<Question> deletedQuestion = questionRepository.findById(savedQuestion.getId());

        // Verificação
        assertThat(deletedQuestion).isNotPresent();
    }
    
    @Test
    @DisplayName("Buscar perguntas por Material ID")
    void testFindByMaterialId() {
        // Cenário
        Material otherMaterial = new Material("Other Material", "Another test material.", MaterialType.LIVRO,
                ConservationStatus.BOM, TransactionType.TROCA, null, null, testUser);
        entityManager.persist(otherMaterial);
        entityManager.flush();

        Question question1 = new Question(null, "Question for material A.", "Q1", testUser, testMaterial);
        Question question2 = new Question(null, "Question for material A, part 2.", "Q2", testUser, testMaterial);
        Question question3 = new Question(null, "Question for material B.", "Q3", testUser, otherMaterial);

        entityManager.persist(question1);
        entityManager.persist(question2);
        entityManager.persist(question3);
        entityManager.flush();

        // Ação
        List<Question> questionsForMaterialA = questionRepository.findByMaterialId(testMaterial.getId());

        // Verificação
        assertThat(questionsForMaterialA).hasSize(2);
        assertThat(questionsForMaterialA.stream().map(Question::getTitle))
                .containsExactlyInAnyOrder("Q1", "Q2");
    }
}