package com.datashare.backend.dto;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class FileResponseDTO {


    private String name;
    private Long size;
    private LocalDateTime expirationDate;
    private String token;
    private LocalDateTime createdAt;
}

// pas de password ici , on ne renvoie jamais un mot de passe au frontend