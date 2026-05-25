// RegisterDTo sert à faire le lien avec le frontEnd. Le front recueille les informations pour 
// créer le compte puis ensuite l'envoie dans le backend par le biais de ce fichier. 

// Plus exactement = le front envoie requête POST /api/register avec mail et mot de passe
// Spring, par le biais de UserController, reçoit le JSON et le convertit en RegisterDTO. 
// Ensuite le Controller va passer le RegisterDTo au Service. 

// L'ordre est le suivant. 
// Front = Controller (vers RegisterDTO) = Service = Repository = Base de données. 


package com.datashare.backend.dto;
// package est le chemin de ce fichier dans le projet

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Data
// data permet de générer automatiquement getEmail(), getPassword(), setEmail etc)
// permet de réduire le code au maximum. 

public class RegisterDTO {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 8)
    private String password;
}



// Ce RegisterDTO n'est pas la pour la protection des données. Mais pour la séparation des responsabilités. 
// La conversion se fait pour travailler en objet Java et pas en JSON. 
// Comme ça le front ne connait pas la structure interne. 
// Le front envoie juste ce qu'il a besoin. 