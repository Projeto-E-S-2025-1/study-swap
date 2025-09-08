package com.studyswap.backend.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.studyswap.backend.dto.MaterialRequestDTO;
import com.studyswap.backend.dto.MaterialResponseDTO;
import com.studyswap.backend.model.ConservationStatus;
import com.studyswap.backend.model.Material;
import com.studyswap.backend.model.MaterialType;
import com.studyswap.backend.model.TransactionType;
import com.studyswap.backend.model.User;
import com.studyswap.backend.repository.MaterialRepository;
import com.studyswap.backend.service.exception.FileStorageException;

@Service
public class MaterialService {
    
    private MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    public MaterialResponseDTO createMaterial(MaterialRequestDTO materialDTO, User user, MultipartFile file){
        Material entity = convertToEntity(materialDTO, user);

        if (file != null && !file.isEmpty()) {
            String fileUrl = storeFile(file);
            entity.setPhoto(fileUrl);
        } else {
            entity.setPhoto("/images/default-photo.png");
        }

        Material saved = materialRepository.save(entity);
        return convertToResponseDTO(saved);
    }

    public MaterialResponseDTO updateMaterial(Long idMaterial, MaterialRequestDTO materialDTO, User user, MultipartFile file) {
        Material entity = materialRepository.findById(idMaterial).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Material não encontrado para edição"));
        
        if (!entity.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para editar este material");
        }

        copyDTOToEntity(materialDTO, entity);

        // Se um novo arquivo for enviado, substitui a imagem
        if (file != null && !file.isEmpty()) {
            String fileUrl = storeFile(file);
            entity.setPhoto(fileUrl);
        }
        
        Material updated = materialRepository.save(entity);
        return convertToResponseDTO(updated);
    }

    public MaterialResponseDTO getMaterialById(Long id) {
        Material material = materialRepository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Material não encontrado para leitura"));
        return convertToResponseDTO(material);
    }

    public List<MaterialResponseDTO> getAllMaterials() {
        return materialRepository.findAll().stream().map(this::convertToResponseDTO).toList();
    }

    public List<MaterialResponseDTO> getMaterialsByUser(User user) {
        List<Material> materials = materialRepository.findByUser(user);
        return materials.stream().map(this::convertToResponseDTO).toList();
    }

    public List<MaterialResponseDTO> getAvailableMaterials() {
        List<Material> materials = materialRepository.findByAvailableTrue();
        return materials.stream().map(this::convertToResponseDTO).toList();
    }

    public List<MaterialResponseDTO> searchMaterials(
        String title,
        MaterialType materialType,
        ConservationStatus conservationStatus,
        TransactionType transactionType
    ) {
        List<Material> materials = materialRepository.searchByFilters(title, materialType, conservationStatus, transactionType);
        return materials.stream().map(this::convertToResponseDTO).toList();
    }

    public void deleteMaterial(Long idMaterial, User user) {
        Material entity = materialRepository.findById(idMaterial).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Material não encontrado para deleção"));
        
        if (!entity.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para remover este material");
        }

        materialRepository.delete(entity);
    }

    private Material convertToEntity(MaterialRequestDTO materialDTO, User user) {
        Material entity = new Material();
        copyDTOToEntity(materialDTO, entity);
        entity.setUser(user);
        return entity;
    }

    // Copia os dados de um MaterialDTO para uma entidade Material
    private void copyDTOToEntity(MaterialRequestDTO materialDTO, Material entity) {
        entity.setTitle(materialDTO.getTitle());
        entity.setDescription(materialDTO.getDescription());
        entity.setMaterialType(materialDTO.getMaterialType());
        entity.setConservationStatus(materialDTO.getConservationStatus());
        entity.setTransactionType(materialDTO.getTransactionType());
        entity.setPrice(materialDTO.getPrice());
        entity.setAvailable(materialDTO.isAvailable());
    }

    private MaterialResponseDTO convertToResponseDTO(Material material) {
        return new MaterialResponseDTO(
            material.getId(),
            material.getTitle(),
            material.getDescription(),
            material.getMaterialType(),
            material.getConservationStatus(),
            material.getTransactionType(),
            material.getPrice(),
            material.getPhoto(),
            material.getUser().getId(),
            material.getUser().getName(),
            material.isAvailable()
        );
    }

    private String storeFile(MultipartFile file) {
        try {
            String uploadDir = "uploads/";
            Files.createDirectories(Paths.get(uploadDir));

            String originalFileName = Paths.get(file.getOriginalFilename()).getFileName().toString();
            String fileName = UUID.randomUUID() + "_" + originalFileName;
            Path path = Paths.get(uploadDir).resolve(fileName).normalize();
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/" + fileName;
        } catch (IOException e) {
            throw new FileStorageException("Erro ao salvar o arquivo", e);
        }
    }
}
