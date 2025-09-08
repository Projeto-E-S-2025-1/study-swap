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

import com.studyswap.backend.dto.MaterialResponseDTO;
import com.studyswap.backend.dto.UserResponseDTO;
import com.studyswap.backend.dto.UserUpdateDTO;
import com.studyswap.backend.model.Material;
import com.studyswap.backend.model.User;
import com.studyswap.backend.repository.MaterialRepository;
import com.studyswap.backend.repository.UserRepository;
import com.studyswap.backend.service.exception.FileStorageException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthService authService; // Para pegar o usuário autenticado
    private MaterialRepository materialRepository;
    public UserService(UserRepository userRepository, AuthService authService, 
    		MaterialRepository materialRepository) {
        this.userRepository = userRepository;
        this.authService = authService;
        this.materialRepository = materialRepository;
    }

    public UserResponseDTO getUserProfile(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getPhotoUrl(),
                user.getInterests(),
                user.getRole()
        );
    }

    public UserResponseDTO updateProfile(UserUpdateDTO dto, MultipartFile file) {
        User user = authService.getAuthenticatedUser(); // garante que só atualiza o próprio perfil

        if (dto.getName() != null && !dto.getName().isBlank()) {
            user.setName(dto.getName());
        }
        if (dto.getInterests() != null) {
            user.setInterests(dto.getInterests());
        }

        if (file != null && !file.isEmpty()) {
            String fileUrl = storeFile(file);
            user.setPhotoUrl(fileUrl);
        }

        User updated = userRepository.save(user);

        return new UserResponseDTO(
                updated.getId(),
                updated.getName(),
                updated.getPhotoUrl(),
                updated.getInterests(),
                updated.getRole()
        );
    }
    public void favoriteMaterial(Long id_material){
    	User loggedUser = authService.getAuthenticatedUser();
    	Material material = materialRepository.findById(id_material).orElseThrow(
    			()-> new ResponseStatusException(
				HttpStatus.NOT_FOUND, "Material não encontrado")
		);
		if(loggedUser.getFavoriteMaterials().contains(material)){ 
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Material já favoritado");
		}
		loggedUser.getFavoriteMaterials().add(material);
		userRepository.save(loggedUser);
    }
    private String storeFile(MultipartFile file){
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