package com.studyswap.backend.unit.service;

import com.studyswap.backend.dto.CreateReportDTO;
import com.studyswap.backend.dto.ReportResponseDTO;
import com.studyswap.backend.model.Material;
import com.studyswap.backend.model.Report;
import com.studyswap.backend.model.ReportReason;
import com.studyswap.backend.model.User;
import com.studyswap.backend.repository.MaterialRepository;
import com.studyswap.backend.repository.ReportRepository;
import com.studyswap.backend.repository.UserRepository;
import com.studyswap.backend.service.AuthService;
import com.studyswap.backend.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private ReportRepository reportRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MaterialRepository materialRepository;
    @Mock
    private AuthService authService;

    @InjectMocks
    private ReportService reportService;

    private User reporterUser;
    private User reportedUser;
    private Material reportedMaterial;
    private CreateReportDTO userReportDto;
    private CreateReportDTO materialReportDto;

    @BeforeEach
    void setUp() {
        // Usuários
        reporterUser = new User("Reporter", "reporter@example.com", "pass", null);
        reporterUser.setId(1L);

        reportedUser = new User("Reported User", "reported@example.com", "pass", null);
        reportedUser.setId(2L);

        // Material
        reportedMaterial = new Material();
        reportedMaterial.setId(10L);
        reportedMaterial.setTitle("Material Problemático");

        // DTOs
        userReportDto = new CreateReportDTO();
        userReportDto.setReportedUserId(2L);
        userReportDto.setReason(ReportReason.SPAM);
        userReportDto.setDescription("Usuário está enviando spam.");

        materialReportDto = new CreateReportDTO();
        materialReportDto.setReportedMaterialId(10L);
        materialReportDto.setReason(ReportReason.FRAUDE);
        materialReportDto.setDescription("Material contém conteúdo ofensivo.");

        // Mock comum a todos os testes
        when(authService.getAuthenticatedUser()).thenReturn(reporterUser);
    }

    // ---------------------- createReport - Cenários de Sucesso ----------------------

    @Test
    void testCreateReport_ForUser_Success() {
        // Arrange
        when(userRepository.findById(2L)).thenReturn(Optional.of(reportedUser));
        when(reportRepository.save(any(Report.class))).thenAnswer(invocation -> {
            Report savedReport = invocation.getArgument(0);
            savedReport.setId(100L); // Simula o salvamento no banco
            return savedReport;
        });

        // Act
        ReportResponseDTO result = reportService.createReport(userReportDto);

        // Assert
        assertNotNull(result);
        assertEquals(reporterUser.getId(), result.getReporterId());
        assertEquals(reportedUser.getId(), result.getReportedUserId());
        assertNull(result.getReportedMaterialId());
        assertEquals(userReportDto.getReason(), result.getReason());
        verify(userRepository, times(1)).findById(2L);
        verify(materialRepository, never()).findById(any());
        verify(reportRepository, times(1)).save(any(Report.class));
    }

    @Test
    void testCreateReport_ForMaterial_Success() {
        // Arrange
        when(materialRepository.findById(10L)).thenReturn(Optional.of(reportedMaterial));
        when(reportRepository.save(any(Report.class))).thenAnswer(invocation -> {
            Report savedReport = invocation.getArgument(0);
            savedReport.setId(101L);
            return savedReport;
        });

        // Act
        ReportResponseDTO result = reportService.createReport(materialReportDto);

        // Assert
        assertNotNull(result);
        assertEquals(reporterUser.getId(), result.getReporterId());
        assertNull(result.getReportedUserId());
        assertEquals(reportedMaterial.getId(), result.getReportedMaterialId());
        assertEquals(materialReportDto.getReason(), result.getReason());
        verify(materialRepository, times(1)).findById(10L);
        verify(userRepository, never()).findById(any());
        verify(reportRepository, times(1)).save(any(Report.class));
    }

    // ---------------------- createReport - Cenários de Falha ----------------------

    @Test
    void testCreateReport_Fail_WhenBothIdsProvided() {
        // Arrange
        CreateReportDTO badDto = new CreateReportDTO();
        badDto.setReportedUserId(2L);
        badDto.setReportedMaterialId(10L);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            reportService.createReport(badDto);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testCreateReport_Fail_WhenNoIdsProvided() {
        // Arrange
        CreateReportDTO badDto = new CreateReportDTO();
        badDto.setReason(ReportReason.SPAM); // CORRIGIDO

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            reportService.createReport(badDto);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testCreateReport_Fail_ReportedUserNotFound() {
        // Arrange
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        userReportDto.setReportedUserId(99L);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            reportService.createReport(userReportDto);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void testCreateReport_Fail_ReportedMaterialNotFound() {
        // Arrange
        when(materialRepository.findById(99L)).thenReturn(Optional.empty());
        materialReportDto.setReportedMaterialId(99L);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            reportService.createReport(materialReportDto);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}