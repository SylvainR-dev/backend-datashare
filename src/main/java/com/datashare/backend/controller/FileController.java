package com.datashare.backend.controller;

import com.datashare.backend.dto.FileRequestDTO;
import com.datashare.backend.dto.FileResponseDTO;
import com.datashare.backend.entities.FileEntity;
import com.datashare.backend.entities.UserEntity;
import com.datashare.backend.service.FileService;
import com.datashare.backend.service.StorageService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;
    private final StorageService storageService;

    public FileController(FileService fileService, StorageService storageService) {
        this.fileService = fileService;
        this.storageService = storageService;
    }

    // Upload avec compte
    @PostMapping
    public ResponseEntity<FileResponseDTO> uploadFile(
            @RequestPart("file") MultipartFile file,
            @RequestPart("metadata") FileRequestDTO fileDTO,
            @AuthenticationPrincipal UserEntity user) throws IOException {

        FileResponseDTO response = fileService.uploadFile(file, fileDTO, user);
        return ResponseEntity.ok(response);
    }

    // Téléchargement via token — récupère les infos du fichier
    @GetMapping("/{token}")
    public ResponseEntity<FileEntity> getFileByToken(
            @PathVariable String token) {

        FileEntity fileEntity = fileService.getFileByToken(token);
        return ResponseEntity.ok(fileEntity);
    }

    // URL pré-signée temporaire pour télécharger le fichier
    @GetMapping("/{token}/download-url")
    public ResponseEntity<String> getDownloadUrl(
            @PathVariable String token) {

        FileEntity fileEntity = fileService.getFileByToken(token);
        String presignedUrl = storageService.generatePresignedUrl(fileEntity.getStoragePath());
        return ResponseEntity.ok(presignedUrl);
    }

    // Historique
    @GetMapping
    public ResponseEntity<List<FileEntity>> getFilesByUser(
            @AuthenticationPrincipal UserEntity user) {

        List<FileEntity> files = fileService.getFilesByUser(user.getId());
        return ResponseEntity.ok(files);
    }

    // Suppression
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(
            @PathVariable Long id,
            @AuthenticationPrincipal UserEntity user) throws IOException {

        fileService.deleteFile(id, user);
        return ResponseEntity.noContent().build();
    }
}