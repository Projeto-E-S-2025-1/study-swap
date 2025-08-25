package com.studyswap.backend.unit.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.studyswap.backend.model.ConservationStatus;
import com.studyswap.backend.model.Material;
import com.studyswap.backend.model.MaterialType;
import com.studyswap.backend.model.Role;
import com.studyswap.backend.model.TransactionType;
import com.studyswap.backend.model.User;
import com.studyswap.backend.repository.MaterialRepository;
import com.studyswap.backend.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName("MaterialRepository Integration Tests")
public class MaterialRepositoryTest {


    @Autowired
    private MaterialRepository materialRepository;


    @Autowired
    private UserRepository userRepository;

    private User testUser;


    @BeforeEach
     void setUp() {
         // Setup de dados de teste antes de cada método de teste
         testUser = new User("Test User", "testuser@example.com", "password", Role.STUDENT);
         userRepository.save(testUser);

        // Limpar o repositório antes de cada teste para garantir isolamento
        materialRepository.deleteAll();
        }

    @Test
    @DisplayName("Salvar material")
    void testSaveMaterial() {
         // Cenário
         Material material = new Material("Test Book", "A test book.", MaterialType.LIVRO,
         ConservationStatus.NOVO, TransactionType.VENDA, 10.0, "/images/default.png", testUser);

         // Ação
         Material savedMaterial = materialRepository.save(material);

        // Verificação
        assertThat(savedMaterial).isNotNull();
        assertThat(savedMaterial.getId()).isNotNull();
        assertThat(savedMaterial.getTitle()).isEqualTo("Test Book");
        }

    @Test
    @DisplayName("Buscar material por ID")
    void testFindById() {
        // Cenário
        Material material = new Material("Physics Book", "Advanced concepts.", MaterialType.LIVRO,
        ConservationStatus.BOM, TransactionType.TROCA, null, "/images/physics.png", testUser);
        Material savedMaterial = materialRepository.save(material);

        // Ação
        Optional<Material> foundMaterial = materialRepository.findById(savedMaterial.getId());

        // Verificação
        assertThat(foundMaterial).isPresent();
        assertThat(foundMaterial.get().getTitle()).isEqualTo("Physics Book");
        }


    @Test
    @DisplayName("Deletar material por ID")
    void testDeleteById() {
        // Cenário
        Material material = new Material("Old Chair", "Used chair for studying.", MaterialType.MOBILIARIO,
        ConservationStatus.VELHO, TransactionType.DOACAO, null, null, testUser);
        Material savedMaterial = materialRepository.save(material);

        // Ação
        materialRepository.deleteById(savedMaterial.getId());
        Optional<Material> deletedMaterial = materialRepository.findById(savedMaterial.getId());

        // Verificação
        assertThat(deletedMaterial).isNotPresent();
    }


    @Test
    @DisplayName("Pesquisar por título")
    void testSearchByTitle() {
        // Cenário
        materialRepository.save(new Material("Java Programming", "A book about Java.", MaterialType.LIVRO, ConservationStatus.BOM, TransactionType.VENDA, 50.0, null, testUser));
        materialRepository.save(new Material("Python for Beginners", "Introductory Python book.", MaterialType.LIVRO, ConservationStatus.NOVO, TransactionType.VENDA, 60.0, null, testUser));

        // Ação
        List<Material> result = materialRepository.searchByFilters("Java", null, null, null);

        // Verificação
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Java Programming");
    }


    @Test
    @DisplayName("Pesquisar por tipo de material")
    void testSearchByMaterialType() {
        // Cenário
        materialRepository.save(new Material("Lab Goggles", "Safety equipment.", MaterialType.EQUIPAMENTO, ConservationStatus.BOM, TransactionType.DOACAO, null, null, testUser));
        materialRepository.save(new Material("Calculus Notes", "Notes for calculus.", MaterialType.APOSTILA, ConservationStatus.RAZOAVEL, TransactionType.TROCA, null, null, testUser));

        // Ação
        List<Material> result = materialRepository.searchByFilters(null, MaterialType.EQUIPAMENTO, null, null);

        // Verificação     
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Lab Goggles");
    }



    @Test
    @DisplayName("Pesquisar por múltiplos filtros")
    void testSearchByMultipleFilters() {
        // Cenário
        materialRepository.save(new Material("Old Math Book", "For sale.", MaterialType.LIVRO, ConservationStatus.VELHO, TransactionType.VENDA, 5.0, null, testUser));
        materialRepository.save(new Material("New Math Textbook", "For donation.", MaterialType.LIVRO, ConservationStatus.NOVO, TransactionType.DOACAO, null, null, testUser));
        materialRepository.save(new Material("Old Chemistry Book", "Also for sale.", MaterialType.LIVRO, ConservationStatus.VELHO, TransactionType.VENDA, 8.0, null, testUser));

        // Ação
        List<Material> result = materialRepository.searchByFilters("Math", MaterialType.LIVRO, ConservationStatus.NOVO, TransactionType.DOACAO);

        // Verificação
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("New Math Textbook");
        assertThat(result.get(0).getMaterialType()).isEqualTo(MaterialType.LIVRO);
        assertThat(result.get(0).getConservationStatus()).isEqualTo(ConservationStatus.NOVO);
        assertThat(result.get(0).getTransactionType()).isEqualTo(TransactionType.DOACAO);
    }
}