package com.datashare.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;
import java.time.LocalDateTime;

// Les annotations @NotBlank et @NotNull vérifient que les données sont correctes avant de les traiter 
// Ici c'est la requête provenant du FrontEnd. Donc on envoie vers le back le nom du fichier, la taille, et la date d'expiration. 

@Data
public class FileRequestDTO {
    @NotBlank
    private String name;


    @NotNull
    private Long size;



    private LocalDateTime expirationDate;

    // Au dernier pas de NotNull car l'utilisateur peut choisir le nombre de jour. 
    // S'il ne choisit rien : 7 jours sont définis par défaut. Détails dans le fichier (FileService)
    
}