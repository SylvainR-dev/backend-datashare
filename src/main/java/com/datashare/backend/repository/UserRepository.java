package com.datashare.backend.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.datashare.backend.entities.UserEntity;

// JpaRepository donne automatiquement des dizaines de méthodes (save, FindById, FindAll etc...)
// optional = le but est de savoir si le conteneur est vide ou non pour éviter le crash. 

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

}