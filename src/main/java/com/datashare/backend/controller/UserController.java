package com.datashare.backend.controller;

import com.datashare.backend.dto.RegisterDTO;
import com.datashare.backend.dto.UserResponseDTO;
import com.datashare.backend.mapper.UserDTOMapper;
import com.datashare.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// RestController = point d'entrée API, reçoit les requêtes HTTP
// RequestMapping = chemin de base de l'API
// RequiredArgsConstructor = injection de dépendances automatique

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserDTOMapper userDTOMapper;

    // PostMapping = reçoit une requête POST sur /api/register
    // @Valid = vérifie que le RegisterDTO est valide (email, password)
    // @RequestBody = convertit le JSON en RegisterDTO automatiquement

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterDTO registerDTO) {
        UserResponseDTO response = userService.register(userDTOMapper.toEntity(registerDTO));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}