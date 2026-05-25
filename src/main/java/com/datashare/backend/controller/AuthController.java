// Le Controller reçoit les informations du front sous forme de JSON, et grâce à @RequestBody Spring convertit automatiquement ce JSON en objet LoginDTO


// Flux détaillé :
// Front (interface connexion)
// → AuthController (reçoit LoginDTO)
//   → AuthService
//     → UserRepository → BDD (cherche l'utilisateur)
//     ← retourne UserEntity
//   ← vérifie le mot de passe avec PasswordEncoder
//   ← génère le token JWT avec JwtService
// ← retourne le token JWT au front


package com.datashare.backend.controller;

// Il n'y a pas besoin de JwtService  car AuthService qui génère le token et le retourne
import com.datashare.backend.dto.LoginDTO;
import com.datashare.backend.service.AuthService;

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
public class AuthController {

    private final AuthService authService;

    // PostMapping = reçoit une requête POST sur /api/login
    // @Valid = vérifie que le RegisterDTO est valide (email, password)
    // @RequestBody = convertit le JSON en RegisterDTO automatiquement

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDTO loginDTO) {
        String token = authService.login(loginDTO);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}