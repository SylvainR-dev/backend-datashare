package com.datashare.backend.controller;


import com.datashare.backend.dto.RegisterDTO;

// Le flux est le suivant : Controller = service = repository = base de données. 
// Donc il faut ces imports suivants 

import com.datashare.backend.dto.UserResponseDTO;
import com.datashare.backend.service.UserService;
import com.datashare.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.datashare.backend.dto.RegisterDTO;
import com.datashare.backend.mapper.UserDTOMapper;

// Les imports liés aux tests

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.BeforeEach;

// SpringBootTest charge le contexte Spring complet
// @AutoConfigureMockMvc configure MockMvc pour simuler les requêtes HTTP


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerTest {

    // Important : mettre la simulation du mail avec @
    // Mot de passe suppérieur à 8 caractères !!!
    private static final String URL = "/api/register";
    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "password123";


    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
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

// La stratégie de test pour la création d'un utilisateur. 

// La différence avec le UserServiceTest c'est qu'ici on teste la couche HTTP

// 1) Vérifier avec des données invalides POST /api/register avec email invalide ou password trop court = répond 400
// 2) Vérifier lorsqu'un utilisateur existe déja
// 3) Enregistrement réussi : OST /api/register avec email + password valides = répond 201

// AAA
// ARRANGE = initialiser tous les entrants
// ACT = exécuter le système à tester
// ASSERT = Valider les sortants


    @Test
    public void registerUserWithoutRequiredData() throws Exception {
        // ARRANGE : j'instancie un registerDTO vide
        RegisterDTO registerDTO = new RegisterDTO();

        // ACT: je simule une requête HTTP POST vers /api/register
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content(objectMapper.writeValueAsString(registerDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                // ASSERT : cela retourne une erreur
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void registerAlreadyExistUser() throws Exception {
        // ARRANGE : on crée un utilisateur complet et on l'enregistre vraiment en base
        // On prends RegisterDTO au lieu de user pour rester dans l'ordre logique du flux
        RegisterDTO existingUser = new RegisterDTO();
        
        existingUser.setEmail(EMAIL);
        existingUser.setPassword(PASSWORD);
        userService.register(userDTOMapper.toEntity(existingUser)); // on l'enregistre en base


        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setEmail(EMAIL);
        registerDTO.setPassword(PASSWORD);


        // ACT : simule une requête HTTP POST avec le DTO du doublon
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content(objectMapper.writeValueAsString(registerDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                // ASSERT : retourne une erreur car l'utilisateur existe déjà
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    public void registerUserSuccessful() throws Exception {
        // ARRANGE : je prépare un DTO complet avec toutes les données requises
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setEmail(EMAIL);
        registerDTO.setPassword(PASSWORD);

        // ACT : je simule une requête HTTP POST
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content(objectMapper.writeValueAsString(registerDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                // ASSERT : retourne positif
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
}