//le LoginDTO sert à faire le lien avec le Front par le biais du controller. Pour permettre la connexion et renvoyer un token JWT



// L'ordre et le flux détaillé. 
// 0 FRONT
// 1) AuthController (reçoit LoginDTO)
// 2) AuthService (vérifie email + password)
// 3) UserRepository (cherche l'utilisateur en base)
// 4) Base de données
// 5) retourne UserEntity
// 6) vérifie le mot de passe avec PasswordEncoder
// 7) génère un token JWT avec JwtService
// 8) retourne le token JWT au front


package com.datashare.backend.dto;
// package est le chemin de ce fichier dans le projet

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
// data permet de générer automatiquement getEmail(), getPassword(), setEmail etc)
// permet de réduire le code au maximum. 

// plus permissif que le register, donc il n'y a pas besoin de mettre @Email, et les restrictions de mot de passe. 
public class LoginDTO {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}