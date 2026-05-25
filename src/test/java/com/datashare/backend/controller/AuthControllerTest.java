package com.datashare.backend.controller;

import com.datashare.backend.dto.LoginDTO;
import com.datashare.backend.repository.UserRepository;
import com.datashare.backend.service.AuthService;
import com.datashare.backend.service.UserService;
import com.datashare.backend.mapper.UserDTOMapper;
import com.datashare.backend.dto.RegisterDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

// Flux détaillé :
// 0) SecurityConfig (vérifie que /api/login est publique → laisse passer)
// 1) Front
// 2) POST /api/login (email + password en JSON)
// 3) AuthController (@RequestBody convertit JSON en LoginDTO)
// 4) AuthService
// 5) UserRepository → BDD (cherche l'utilisateur par email)
// 6) retourne UserEntity
// 7) vérifie le mot de passe avec PasswordEncoder
// 8) génère le token JWT avec JwtService
// 9) retourne le token JWT
// 10) 200 OK + token JWT

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthControllerTest {

    private static final String URL = "/api/login";
    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "password123";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDTOMapper userDTOMapper;

    @BeforeEach
    public void beforeEach() {
        userRepository.deleteAll();
    }

    @AfterEach
    public void afterEach() {
        userRepository.deleteAll();
    }

    // Stratégie de test :
    // 1) Connexion avec des données invalides → 400
    // 2) Connexion avec un utilisateur inexistant → 400
    // 3) Connexion réussie → 200 + token JWT

    @Test
    public void loginWithoutRequiredData() throws Exception {
        // ARRANGE : LoginDTO vide
        LoginDTO loginDTO = new LoginDTO();

        // ACT : POST /api/login avec DTO vide
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content(objectMapper.writeValueAsString(loginDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                // ASSERT : 400 Bad Request
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void loginWithNonExistentUser() throws Exception {
        // ARRANGE : LoginDTO avec un utilisateur qui n'existe pas en base
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(EMAIL);
        loginDTO.setPassword(PASSWORD);

        // ACT : POST /api/login
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content(objectMapper.writeValueAsString(loginDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                // ASSERT : 400 Bad Request
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void loginSuccessful() throws Exception {
        // ARRANGE : on crée un utilisateur en base puis on tente la connexion
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setEmail(EMAIL);
        registerDTO.setPassword(PASSWORD);
        userService.register(userDTOMapper.toEntity(registerDTO));

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(EMAIL);
        loginDTO.setPassword(PASSWORD);

        // ACT : POST /api/login
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content(objectMapper.writeValueAsString(loginDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                // ASSERT : 200 OK + token JWT non vide
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.emptyString())));
    }
}