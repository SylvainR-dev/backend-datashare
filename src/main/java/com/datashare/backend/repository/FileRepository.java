package com.datashare.backend.repository;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.datashare.backend.entities.FileEntity;

import java.util.List;


@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    
    Optional<FileEntity> findByToken(String token);

    List<FileEntity> findByUserId(Long userId);

}
