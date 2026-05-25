// Attention
// RegisterDTO est la structure de données que le front envoie (email et mot de passe). 
// UserDTOMapper est un convertisseur qui transforme le RegisterDTO en UserEntity.


package com.mapper;

import com.datashare.backend.dto.RegisterDTO;
import com.datashare.backend.entities.UserEntity;
import org.springframework.stereotype.Component;

// Component = dit à Spring que cette classe est un composant injectable
// composant injectable = créé un objet automatiquement
// toEntity = convertit le RegisterDTO en UserEntity


@Component
public class UserDTOMapper {

    public UserEntity toEntity(RegisterDTO registerDTO) {
        UserEntity user = new UserEntity();
        user.setEmail(registerDTO.getEmail());
        user.setPassword(registerDTO.getPassword());
        return user;
    }
}