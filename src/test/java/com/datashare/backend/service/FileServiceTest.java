package com.datashare.backend.service;


import com.datashare.backend.service.FileService;
import com.datashare.backend.repository.FileRepository;
import com.datashare.backend.dto.FileResponseDTO;
import com.datashare.backend.dto.FileRequestDTO;
import com.datashare.backend.entities.FileEntity;

import com.datashare.backend.entities.UserEntity;
import com.datashare.backend.service.StorageService;


// Ensuite tous les imports liés au tests. 

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;

// comme prévu on retrouve les exceptions, liste et optional. 

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class FileServiceTest {

    private static final String TOKEN = "fake.jwt.token";

    @Mock
    private FileRepository fileRepository;
    @Mock
    private StorageService storageService;
    @Mock
    private MultipartFile multipartFile;
    @InjectMocks
    private FileService fileService;

    // Arrange = initialiser tous les entrants
    // Act = exécuter le système à tester
    // Assert = valider les sortants

    // Test 1 — Upload d'un fichier
    @Test
    public void testUploadFile() throws IOException {

        // Prépare un utilisateur fictif
        UserEntity user = new UserEntity();
        user.setId(1L);

        // Prépare le DTO de requête
        FileRequestDTO fileRequestDTO = new FileRequestDTO();
        fileRequestDTO.setName("test.pdf");
        fileRequestDTO.setSize(1024L);

        // Simule le comportement de StorageService
        when(storageService.saveFile(any())).thenReturn("unique_test.pdf");

        // Simule le comportement de FileRepository
        FileEntity savedEntity = new FileEntity();
        savedEntity.setToken("mon-token-unique");
        savedEntity.setName("test.pdf");
        savedEntity.setSize(1024L);
        savedEntity.setExpirationDate(LocalDateTime.now().plusDays(7));
        savedEntity.setCreatedAt(LocalDateTime.now());
        when(fileRepository.save(any())).thenReturn(savedEntity);

        // Appelle la méthode à tester
        FileResponseDTO response = fileService.uploadFile(multipartFile, fileRequestDTO, user);

        // Vérifie que la réponse est correcte
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("mon-token-unique");
        assertThat(response.getName()).isEqualTo("test.pdf");

        // Vérifie que les méthodes ont bien été appelées
        verify(storageService).saveFile(any());
        verify(fileRepository).save(any());
    }

    // Test 2 — Récupérer un fichier par token
    @Test
    public void testGetFileByToken() {

        // Prépare un fichier fictif en base
        FileEntity fileEntity = new FileEntity();
        fileEntity.setToken("mon-token-unique");
        fileEntity.setName("test.pdf");

        // Simule la recherche en base
        when(fileRepository.findByToken("mon-token-unique")).thenReturn(Optional.of(fileEntity));

        // Appelle la méthode à tester
        FileEntity result = fileService.getFileByToken("mon-token-unique");

        // Vérifie le résultat
        assertThat(result).isNotNull();
        assertThat(result.getToken()).isEqualTo("mon-token-unique");
        assertThat(result.getName()).isEqualTo("test.pdf");
    }

    // Test 3 — Récupérer les fichiers d'un utilisateur
    @Test
    public void testGetFilesByUser() {

        // Prépare une liste de fichiers fictifs
        FileEntity file1 = new FileEntity();
        file1.setName("fichier1.pdf");

        FileEntity file2 = new FileEntity();
        file2.setName("fichier2.pdf");

        // Simule la recherche en base
        when(fileRepository.findByUserId(1L)).thenReturn(List.of(file1, file2));

        // Appelle la méthode à tester
        List<FileEntity> result = fileService.getFilesByUser(1L);

        // Vérifie le résultat
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("fichier1.pdf");
        assertThat(result.get(1).getName()).isEqualTo("fichier2.pdf");
    }

    // Test 4 — Supprimer un fichier
    @Test
    public void testDeleteFile() throws IOException {

        // Prépare un utilisateur fictif
        UserEntity user = new UserEntity();
        user.setId(1L);

        // Prépare un fichier fictif en base
        FileEntity fileEntity = new FileEntity();
        fileEntity.setId(1L);
        fileEntity.setStoragePath("unique_test.pdf");
        fileEntity.setUser(user);

        // Simule la recherche en base
        when(fileRepository.findById(1L)).thenReturn(Optional.of(fileEntity));

        // Appelle la méthode à tester
        fileService.deleteFile(1L, user);

        // Vérifie que la suppression a bien eu lieu
        verify(storageService).deleteFile("unique_test.pdf");
        verify(fileRepository).delete(fileEntity);
    }
}
