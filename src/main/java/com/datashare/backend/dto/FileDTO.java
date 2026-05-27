package com.datashare.backend.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FileDTO {
    @NotBlank
    private String name;


    @NotNull
    private Long size;
    @NotNull
    private String password;
    @NotNull
    private LocalDateTime expirationDate;

}