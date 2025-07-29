package com.studyswap.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.studyswap.backend.dto.MaterialRequestDTO;
import com.studyswap.backend.dto.MaterialResponseDTO;
import com.studyswap.backend.model.Material;
import com.studyswap.backend.model.User;
import com.studyswap.backend.repository.MaterialRepository;

@Service
public class MaterialService {
    @Autowired
    private MaterialRepository materialRepository;

    public MaterialResponseDTO createMaterial(MaterialRequestDTO materialDTO, User user){
        Material entity = convertToEntity(materialDTO, user);
        Material saved = materialRepository.save(entity);
        return convertToResponseDTO(saved);
    }

    public MaterialResponseDTO updateMaterial(Long idMaterial, MaterialRequestDTO materialDTO, User user) {
        Material entity = materialRepository.findById(idMaterial).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Material não encontrado"));
        
        if (!entity.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para editar este material");
        }

        copyDTOToEntity(materialDTO, entity);
        Material updated = materialRepository.save(entity);
        return convertToResponseDTO(updated);
    }

    public MaterialResponseDTO getMaterialById(Long id) {
        Material material = materialRepository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Material não encontrado"));
        return convertToResponseDTO(material);
    }

    public List<MaterialResponseDTO> getAllMaterials() {
        return materialRepository.findAll().stream().map(this::convertToResponseDTO).toList();
    }

    public void deleteMaterial(Long idMaterial, User user) {
        Material entity = materialRepository.findById(idMaterial).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Material não encontrado"));
        
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
        entity.setPhoto(materialDTO.getPhoto());
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
            material.getUser().getName()
        );
    }
}
