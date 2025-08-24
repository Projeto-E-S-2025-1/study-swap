package com.studyswap.backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.studyswap.backend.dto.CreateReportDTO;
import com.studyswap.backend.dto.ReportResponseDTO;
import com.studyswap.backend.model.Material;
import com.studyswap.backend.model.Report;
import com.studyswap.backend.model.User;
import com.studyswap.backend.repository.MaterialRepository;
import com.studyswap.backend.repository.ReportRepository;
import com.studyswap.backend.repository.UserRepository;

@Service
public class ReportService {

    private ReportRepository reportRepository;

    private UserRepository userRepository;

    private MaterialRepository materialRepository;

    private AuthService authService;

    public ReportService(ReportRepository reportRepository, UserRepository userRepository, MaterialRepository materialRepository, AuthService authService) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.materialRepository = materialRepository;
        this.authService = authService;
    }

    public ReportResponseDTO createReport(CreateReportDTO dto) {
        User reporter = authService.getAuthenticatedUser();

        boolean hasUser = dto.getReportedUserId() != null;
        boolean hasMaterial = dto.getReportedMaterialId() != null;

        if (hasUser == hasMaterial) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "A denúncia deve ser contra apenas um tipo: usuário ou material.");
        }

        Report report = new Report();
        report.setReporter(reporter);
        report.setReason(dto.getReason());
        report.setDescription(dto.getDescription());

        if (dto.getReportedUserId() != null) {
            User reportedUser = userRepository.findById(dto.getReportedUserId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário denunciado não encontrado"));
            report.setReportedUser(reportedUser);
        }

        if (dto.getReportedMaterialId() != null) {
            Material reportedMaterial = materialRepository.findById(dto.getReportedMaterialId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Material denunciado não encontrado"));
            report.setReportedMaterial(reportedMaterial);
        }

        Report saved = reportRepository.save(report);
        return convertToResponseDTO(saved);
    }

    private ReportResponseDTO convertToResponseDTO(Report report) {
        return new ReportResponseDTO(
                report.getId(),
                report.getReporter().getId(),
                report.getReportedUser() != null ? report.getReportedUser().getId() : null,
                report.getReportedMaterial() != null ? report.getReportedMaterial().getId() : null,
                report.getReason(),
                report.getDescription()
        );
    }
}
