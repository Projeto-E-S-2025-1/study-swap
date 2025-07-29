package com.studyswap.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.studyswap.backend.dto.MaterialRequestDTO;
import com.studyswap.backend.dto.MaterialResponseDTO;
import com.studyswap.backend.model.User;
import com.studyswap.backend.service.AuthService;
import com.studyswap.backend.service.MaterialService;

@RestController
@RequestMapping("/material")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @Autowired
    private AuthService authService;

    @PostMapping
    public ResponseEntity<MaterialResponseDTO> createMaterial(@Validated @RequestBody MaterialRequestDTO materialDTO) {
        User user = authService.getAuthenticatedUser();
        MaterialResponseDTO savedMaterial = materialService.createMaterial(materialDTO, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMaterial);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaterialResponseDTO> updateMaterial(
            @PathVariable Long id,
            @Validated @RequestBody MaterialRequestDTO materialDTO) {
        User user = authService.getAuthenticatedUser();
        MaterialResponseDTO updatedMaterial = materialService.updateMaterial(id, materialDTO, user);
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

    @GetMapping
    public List<MaterialResponseDTO> getAllMaterials() {
        return materialService.getAllMaterials();
    }
}
