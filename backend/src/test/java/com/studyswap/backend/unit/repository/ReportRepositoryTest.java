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
import com.studyswap.backend.model.Report;
import com.studyswap.backend.model.ReportReason;
import com.studyswap.backend.model.Role;
import com.studyswap.backend.model.TransactionType;
import com.studyswap.backend.model.User;
import com.studyswap.backend.repository.ReportRepository;

import java.util.Optional;

@DataJpaTest
@DisplayName("ReportRepository Integration Tests")
class ReportRepositoryTest {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User reporterUser;
    private User reportedUser;
    private Material reportedMaterial;

    @BeforeEach
    void setUp() {
        // Configura dados de teste para os relacionamentos
        reporterUser = new User("Reporter Test", "reporter@example.com", "password", Role.STUDENT);
        reportedUser = new User("Reported Test", "reported@example.com", "password", Role.STUDENT);
        reportedMaterial = new Material("Test Material", "Material for a report.", MaterialType.LIVRO,
                ConservationStatus.BOM, TransactionType.VENDA, 20.0, "/images/test.png", reportedUser);

        // Persiste as entidades dependentes para que possam ser referenciadas
        entityManager.persist(reporterUser);
        entityManager.persist(reportedUser);
        entityManager.persist(reportedMaterial);
        entityManager.flush();
    }

    @Test
    @DisplayName("Salvar uma denúncia de usuário")
    void testSaveUserReport() {
        // Cenário: Cria uma denúncia contra um usuário
        Report report = new Report(reporterUser, reportedUser, null, ReportReason.CONTEUDO_IMPROPRIO, "User posted inappropriate content.");

        // Ação
        Report savedReport = reportRepository.save(report);

        // Verificação
        assertThat(savedReport).isNotNull();
        assertThat(savedReport.getId()).isNotNull();
        assertThat(savedReport.getReporter().getEmail()).isEqualTo("reporter@example.com");
        assertThat(savedReport.getReportedUser()).isEqualTo(reportedUser);
        assertThat(savedReport.getReportedMaterial()).isNull();
        assertThat(savedReport.getReason()).isEqualTo(ReportReason.CONTEUDO_IMPROPRIO);
    }

    @Test
    @DisplayName("Salvar uma denúncia de material")
    void testSaveMaterialReport() {
        // Cenário: Cria uma denúncia contra um material
        Report report = new Report(reporterUser, null, reportedMaterial, ReportReason.FRAUDE, "Material is not as described.");

        // Ação
        Report savedReport = reportRepository.save(report);

        // Verificação
        assertThat(savedReport).isNotNull();
        assertThat(savedReport.getId()).isNotNull();
        assertThat(savedReport.getReporter().getEmail()).isEqualTo("reporter@example.com");
        assertThat(savedReport.getReportedMaterial()).isEqualTo(reportedMaterial);
        assertThat(savedReport.getReportedUser()).isNull();
        assertThat(savedReport.getReason()).isEqualTo(ReportReason.FRAUDE);
    }

    @Test
    @DisplayName("Buscar denúncia por ID")
    void testFindById() {
        // Cenário
        Report report = new Report(reporterUser, reportedUser, null, ReportReason.SPAM, "Spamming comments.");
        Report savedReport = entityManager.persist(report);
        entityManager.flush();

        // Ação
        Optional<Report> foundReport = reportRepository.findById(savedReport.getId());

        // Verificação
        assertThat(foundReport).isPresent();
        assertThat(foundReport.get().getReason()).isEqualTo(ReportReason.SPAM);
    }

    @Test
    @DisplayName("Deletar denúncia por ID")
    void testDeleteById() {
        // Cenário
        Report report = new Report(reporterUser, reportedUser, null, ReportReason.SPAM, "Spamming comments.");
        Report savedReport = entityManager.persist(report);
        entityManager.flush();

        // Ação
        reportRepository.deleteById(savedReport.getId());
        Optional<Report> deletedReport = reportRepository.findById(savedReport.getId());

        // Verificação
        assertThat(deletedReport).isNotPresent();
    }
}