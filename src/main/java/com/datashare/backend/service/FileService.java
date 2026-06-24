package com.datashare.backend.service;

import com.datashare.backend.dto.FileRequestDTO;
import com.datashare.backend.dto.FileResponseDTO;
import com.datashare.backend.entities.FileEntity;
import com.datashare.backend.entities.UserEntity;
import com.datashare.backend.repository.FileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {

    private final FileRepository fileRepository;
    private final StorageService storageService;

    private static final List<String> FORBIDDEN_EXTENSIONS = List.of(
            ".exe", ".bat", ".sh", ".cmd", ".msi", ".vbs", ".ps1"
    );

    private static final long MAX_FILE_SIZE = 1_073_741_824L; // 1 Go en bytes

    public FileService(FileRepository fileRepository, StorageService storageService) {
        this.fileRepository = fileRepository;
        this.storageService = storageService;
    }

    public FileResponseDTO uploadFile(MultipartFile file, FileRequestDTO fileRequestDTO, UserEntity user) throws IOException {

        // 1. Vérifie l'extension du fichier
        String fileName = file.getOriginalFilename();
        if (fileName != null) {
            String lowerCaseName = fileName.toLowerCase();
            boolean isForbidden = FORBIDDEN_EXTENSIONS.stream().anyMatch(lowerCaseName::endsWith);
            if (isForbidden) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Extension de fichier interdite");
            }
        }

        // 2. Vérifie la taille du fichier
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fichier trop volumineux (max 1 Go)");
        }

        // 3. Sauvegarde le fichier sur S3
        String storagePath = storageService.saveFile(file);

        // 4. Génère un token unique pour le lien de téléchargement
        String token = UUID.randomUUID().toString();

        // 5. Calcule la date d'expiration (7 jours par défaut)
        LocalDateTime expirationDate = fileRequestDTO.getExpirationDate() != null
                ? fileRequestDTO.getExpirationDate()
                : LocalDateTime.now().plusDays(7);

        // 6. Construit l'entité à sauvegarder en base
        FileEntity fileEntity = new FileEntity();
        fileEntity.setName(file.getOriginalFilename());
        fileEntity.setSize(file.getSize());
        fileEntity.setToken(token);
        fileEntity.setStoragePath(storagePath);
        fileEntity.setExpirationDate(expirationDate);
        fileEntity.setUser(user);

        // 7. Sauvegarde les métadonnées en base
        FileEntity saved = fileRepository.save(fileEntity);

        // 8. Construit et retourne la réponse
        FileResponseDTO response = new FileResponseDTO();
        response.setToken(saved.getToken());
        response.setName(saved.getName());
        response.setSize(saved.getSize());
        response.setExpirationDate(saved.getExpirationDate());
        response.setCreatedAt(saved.getCreatedAt());

        return response;
    }

    public FileEntity getFileByToken(String token) {
        return fileRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Fichier introuvable ou lien expiré"));
    }

    public List<FileEntity> getFilesByUser(Long userId) {
        return fileRepository.findByUserId(userId);
    }

    public void deleteFile(Long id, UserEntity user) throws IOException {
        FileEntity fileEntity = fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fichier introuvable"));

        // Vérifie que l'utilisateur est bien le propriétaire
        if (!fileEntity.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Vous n'êtes pas autorisé à supprimer ce fichier");
        }

        // Supprime sur S3
        storageService.deleteFile(fileEntity.getStoragePath());

        // Supprime en base
        fileRepository.delete(fileEntity);
    }
}