package com.datashare.backend.controller;

import com.datashare.backend.dto.FileRequestDTO;
import com.datashare.backend.dto.FileResponseDTO;
import com.datashare.backend.entities.FileEntity;
import com.datashare.backend.entities.UserEntity;
import com.datashare.backend.service.FileService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
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

    // Téléchargement via token
    @GetMapping("/{token}")
    public ResponseEntity<FileEntity> getFileByToken(
            @PathVariable String token) {

        FileEntity fileEntity = fileService.getFileByToken(token);
        return ResponseEntity.ok(fileEntity);
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