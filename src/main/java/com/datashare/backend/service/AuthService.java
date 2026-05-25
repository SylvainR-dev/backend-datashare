// AuthService gère la logique métier de la connexion

// La logique métier est quoi : 
// C'est le fichier qui prends la décision.
// Il regarde si le mail existe, si le mot de passe est ok, si tout bon renvoie un token JWT

// Flux : LoginDTO = cherche l'utilisateur en base = vérifie le mot de passe = génère le token JWT

// Flux détaillé : 
// 1) Front (interface connexion)
// 2) AuthController (reçoit LoginDTO)
// 3) AuthService
// 3.1) UserRepository → BDD (cherche l'utilisateur)
//3.2 ) retourne UserEntity
// 4) vérifie le mot de passe avec PasswordEncoder
// 5) génère le token JWT avec JwtService
// 6) retourne le token JWT au front


package com.datashare.backend.service;

import com.datashare.backend.dto.LoginDTO;
import com.datashare.backend.entities.UserEntity;
import com.datashare.backend.repository.UserRepository;
import com.datashare.backend.security.JwtService;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;




@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String login(LoginDTO loginDTO) {
        log.info("Login attempt for email: {}", loginDTO.getEmail());

        // 1. Cherche l'utilisateur en base par email
        Optional<UserEntity> optionalUser = userRepository.findByEmail(loginDTO.getEmail());
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        UserEntity user = optionalUser.get();

        // 2. Vérifie le mot de passe
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        // 3. Génère et retourne le token JWT
        log.info("Login successful for email: {}", loginDTO.getEmail());
        return jwtService.generateToken(user);
    }
}