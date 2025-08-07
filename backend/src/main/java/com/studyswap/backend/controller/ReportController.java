package com.studyswap.backend.controller;

import com.studyswap.backend.dto.CreateReportDTO;
import com.studyswap.backend.dto.ReportResponseDTO;
import com.studyswap.backend.service.ReportService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping
    public ResponseEntity<ReportResponseDTO> createReport(@Valid @RequestBody CreateReportDTO dto) {
        ReportResponseDTO response = reportService.createReport(dto);
        return ResponseEntity.status(201).body(response);
    }
}
