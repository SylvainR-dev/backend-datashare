
// Pour toute explication détaillée, voir d'abord le fichier UserEntity et comparer. 

package com.datashare.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "files")
public class FileEntity {

// Ici ce sont les tables que l'on retrouvera dans la base de donnée. 
// Attention : ce n'est pas la base de données qui définit les tables, mais Entity (avec JAVA) 
// Parce que grâce au fichier "application.yaml" (dans ressources), on peut configurer Spring Boot pour qu'il crée automatiquement les tables en base de données à partir de ces entités !!!!!




// @GeneratedValue = il dit à Spring Boot comment générer automatiquement la valeur de l'ID
// strategy = GenerationType.IDENTITY | ici c'est la base de données qui se charge de l'auto-incrémentation. C'est à dire que le premier fichier aura attribué l'id = 1 , ainsi de suite. 


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

// ici dans le nom comme dans le size il n'y a pas de (unique = true). Logique car il peut y avoir le même nom et la même taille parfois. 

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

// ici comme il y aura une taille de fichier, il faut mettre @NotNull, et en dessous Long. 
// Long = type de données pour stocker le nombre d'octets.C'est Spring Boot qui refuse le fichier si il dépasse 1 Go, pas le type Long.

    @NotNull
    @Column(name= "size", nullable = false)
    private Long size;

    @NotBlank
    @Column(name= "token", unique = true, nullable = false)
    private String token;


    @Column(name = "password")
    private String password;

    @NotNull
    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

// Un utilisateur peut avoir plusieurs fichiers mais un fichier appartient à un seul utilisateur. 
// Raison pour laquelle il y a @ManyToOne , auquel on joint la colonne de l'user avec son ID.

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

//storagePath c'est le nom unique du fichier sur S3

    @Column(name = "storage_path", nullable = false)
    private String storagePath;
    
}
