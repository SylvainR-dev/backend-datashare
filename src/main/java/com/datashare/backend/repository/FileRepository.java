package com.datashare.backend.repository;


import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.datashare.backend.entities.FileEntity;



// Concrètement FileRepository va dire : "Va chercher dans la table files la ligne où token = ..."


@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    
    Optional<FileEntity> findByToken(String token);

    List<FileEntity> findByUserId(Long userId);

// On garde la même logique. Un fichier ne peut avoir qu'un seul token. Donc je mets Optional. 
// Mais un utilisateur peut avoir plusieurs fichiers. Donc il faut retourner une liste. 
// Ensuite dans le code je mets FileEntity pour faire le lien avec les tables de la base de données. Logique; 
// Plus précisément FilenEntity dit à Spring quel type d'objet retourner. 

// findByToken et findByUserId sont des méthodes, optional et list sont les types de retour. 
// Exemple la méthode est le verbe chercher, le type de retour et le résultat = un fichier ou liste de fichiers. 

}
