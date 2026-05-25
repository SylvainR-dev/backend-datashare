package com.datashare.backend.dto;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private String email;
    private LocalDateTime createdAt;

}