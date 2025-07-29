package com.studyswap.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.studyswap.backend.dto.MaterialDTO;
import com.studyswap.backend.model.Material;
import com.studyswap.backend.model.User;
import com.studyswap.backend.repository.MaterialRepository;

@Service
public class MaterialService {
    @Autowired
    private MaterialRepository materialRepository;

    public Material createMaterial(MaterialDTO materialDTO, User user){
        Material entity = convertToEntity(materialDTO, user);
        return materialRepository.save(entity);
    }

    public Material updateMaterial(Long idMaterial, MaterialDTO materialDTO) {
        Material entity = materialRepository.findById(idMaterial).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Material não encontrado"));
        
        copyDTOToEntity(materialDTO, entity);
        return materialRepository.save(entity);
    }

    public Material getMaterialById(Long id) {
        return materialRepository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Material não encontrado"));
    }

    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }

    public void deleteMaterial(Long id) {
        Material entity = materialRepository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Material não encontrado"));
        
        materialRepository.delete(entity);
    }

    private Material convertToEntity(MaterialDTO materialDTO, User user) {
        Material entity = new Material();
        copyDTOToEntity(materialDTO, entity);
        entity.setUser(user);
        return entity;
    }

    // Copia os dados de um MaterialDTO para uma entidade Material
    private void copyDTOToEntity(MaterialDTO materialDTO, Material entity) {
        entity.setTitle(materialDTO.getTitle());
        entity.setDescription(materialDTO.getDescription());
        entity.setMaterialType(materialDTO.getMaterialType());
        entity.setConservationStatus(materialDTO.getConservationStatus());
        entity.setTransactionType(materialDTO.getTransactionType());
        entity.setPrice(materialDTO.getPrice());
        entity.setPhoto(materialDTO.getPhoto());
    }
}
