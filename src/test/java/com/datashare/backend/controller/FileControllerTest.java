package com.datashare.backend.controller;

import com.datashare.backend.entities.FileEntity;
import com.datashare.backend.entities.UserEntity;
import com.datashare.backend.repository.FileRepository;
import com.datashare.backend.repository.UserRepository;
import com.datashare.backend.security.JwtService;
import com.datashare.backend.service.StorageService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private StorageService storageService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    private UserEntity testUser;
    private String token;

    @BeforeEach
    public void beforeEach() {
        fileRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new UserEntity();
        testUser.setEmail("test@test.com");
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser = userRepository.save(testUser);

        token = jwtService.generateToken(testUser);
    }

    @AfterEach
    public void afterEach() {
        fileRepository.deleteAll();
        userRepository.deleteAll();
    }

    // Test 1 — Upload d'un fichier
    @Test
    public void testUploadFile() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.pdf",
            MediaType.APPLICATION_PDF_VALUE,
            "contenu fictif".getBytes()
        );


        // Ajout des métadonnées JSON
        MockMultipartFile metadata = new MockMultipartFile(
            "metadata",
            "",
            MediaType.APPLICATION_JSON_VALUE,
            "{\"name\":\"test.pdf\",\"size\":1024}".getBytes()
        );

        when(storageService.saveFile(any())).thenReturn("unique_test.pdf");

        mockMvc.perform(multipart("/files")
                .file(file)
                .file(metadata)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test.pdf"))
                .andExpect(jsonPath("$.token").exists());
    }

    // Test 2 — Téléchargement via token
    @Test
    public void testGetFileByToken() throws Exception {

        FileEntity fileEntity = new FileEntity();
        fileEntity.setName("test.pdf");
        fileEntity.setSize(1024L);
        fileEntity.setToken("mon-token-unique");
        fileEntity.setStoragePath("unique_test.pdf");
        fileEntity.setExpirationDate(LocalDateTime.now().plusDays(7));
        fileRepository.save(fileEntity);

        mockMvc.perform(get("/files/mon-token-unique")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mon-token-unique"))
                .andExpect(jsonPath("$.name").value("test.pdf"));
    }

    // Test 3 — Historique
    @Test
    public void testGetFilesByUser() throws Exception {

        mockMvc.perform(get("/files")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    // Test 4 — Suppression
    @Test
    public void testDeleteFile() throws Exception {

        FileEntity fileEntity = new FileEntity();
        fileEntity.setName("test.pdf");
        fileEntity.setSize(1024L);
        fileEntity.setToken("mon-token-unique");
        fileEntity.setStoragePath("unique_test.pdf");
        fileEntity.setExpirationDate(LocalDateTime.now().plusDays(7));
        fileEntity.setUser(testUser);
        FileEntity saved = fileRepository.save(fileEntity);

        doNothing().when(storageService).deleteFile(any());

        mockMvc.perform(delete("/files/" + saved.getId())
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }
}