
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
// Attention : ce n'est pas la base de données qui définit les tables, mais Entity. 
// Parce que grâce au fichier "application.yaml" (dans ressources), on peut configurer Spring Boot pour qu'il crée automatiquement les tables en base de données à partir de ces entités !!!!!

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name= "size", nullable = false)
    private Long size;

    @NotBlank
    @Column(name= "token", unique = true, nullable = false)
    private String token;

    @NotBlank
    @Column(name = "password", nullable = false)
    private String password;

    @NotBlank
    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "storage_path", nullable = false)
    private String storagePath;
    
}
