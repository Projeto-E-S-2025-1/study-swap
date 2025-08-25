package com.studyswap.backend.unit.model;

import com.studyswap.backend.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReportTest {

    @Test
    void testGettersAndSetters() {
        // Criar usuários e material mock
        User reporter = new User();
        reporter.setId(1L);
        reporter.setName("Reporter");
        reporter.setEmail("reporter@email.com");

        User reportedUser = new User();
        reportedUser.setId(2L);
        reportedUser.setName("Reported");
        reportedUser.setEmail("reported@email.com");

        Material material = new Material();
        material.setId(10L);
        material.setTitle("Livro Java");

        // Criar Report usando construtor
        Report report = new Report(
                reporter,
                reportedUser,
                material,
                ReportReason.SPAM,
                "Descrição da denúncia"
        );

        // Verificar construtor
        assertEquals(reporter, report.getReporter());
        assertEquals(reportedUser, report.getReportedUser());
        assertEquals(material, report.getReportedMaterial());
        assertEquals(ReportReason.SPAM, report.getReason());
        assertEquals("Descrição da denúncia", report.getDescription());

        // Testar setters
        report.setId(100L);
        report.setReporter(reportedUser);
        report.setReportedUser(reporter);
        report.setReportedMaterial(null);
        report.setReason(ReportReason.CONTEUDO_IMPROPRIO);
        report.setDescription("Nova descrição");

        // Testar getters
        assertEquals(100L, report.getId());
        assertEquals(reportedUser, report.getReporter());
        assertEquals(reporter, report.getReportedUser());
        assertNull(report.getReportedMaterial());
        assertEquals(ReportReason.CONTEUDO_IMPROPRIO, report.getReason());
        assertEquals("Nova descrição", report.getDescription());
    }

    @Test
    void testDefaultConstructor() {
        Report report = new Report();
        assertNull(report.getId());
        assertNull(report.getReporter());
        assertNull(report.getReportedUser());
        assertNull(report.getReportedMaterial());
        assertNull(report.getReason());
        assertNull(report.getDescription());
    }
}
