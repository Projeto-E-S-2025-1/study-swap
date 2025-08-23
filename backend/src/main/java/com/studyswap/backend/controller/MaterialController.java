package com.studyswap.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.studyswap.backend.dto.MaterialRequestDTO;
import com.studyswap.backend.dto.MaterialResponseDTO;
import com.studyswap.backend.model.ConservationStatus;
import com.studyswap.backend.model.MaterialType;
import com.studyswap.backend.model.TransactionType;
import com.studyswap.backend.model.User;
import com.studyswap.backend.service.AuthService;
import com.studyswap.backend.service.MaterialService;

@RestController
@RequestMapping("/material")

public class MaterialController {

    private MaterialService materialService;

    private AuthService authService;

    public MaterialController(MaterialService materialService, AuthService authService){
        this.materialService = materialService;
        this.authService = authService;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<MaterialResponseDTO> createMaterial(
            @RequestPart("materialDTO") @Validated MaterialRequestDTO materialDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        User user = authService.getAuthenticatedUser();
        MaterialResponseDTO savedMaterial = materialService.createMaterial(materialDTO, user, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMaterial);
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<MaterialResponseDTO> updateMaterial(
            @PathVariable Long id,
            @RequestPart("materialDTO") @Validated MaterialRequestDTO materialDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        User user = authService.getAuthenticatedUser();
        MaterialResponseDTO updatedMaterial = materialService.updateMaterial(id, materialDTO, user, file);
        return ResponseEntity.status(HttpStatus.OK).body(updatedMaterial);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMaterial(@PathVariable Long id) {
        User user = authService.getAuthenticatedUser();
        materialService.deleteMaterial(id, user);
        return ResponseEntity.status(HttpStatus.OK).body("Material deletado com sucesso!");
    }

    @GetMapping("/{id}")
    public MaterialResponseDTO getMaterialById(@PathVariable Long id) {
        return materialService.getMaterialById(id);
    }

    @GetMapping("/search")
    public List<MaterialResponseDTO> searchMaterials(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) MaterialType materialType,
            @RequestParam(required = false) ConservationStatus conservationStatus,
            @RequestParam(required = false) TransactionType transactionType
    ) {
        return materialService.searchMaterials(title, materialType, conservationStatus, transactionType);
    }

    @GetMapping
    public List<MaterialResponseDTO> getAllMaterials() {
        return materialService.getAllMaterials();
    }
}
