package com.datashare.backend.service;

import com.datashare.backend.dto.UserResponseDTO;
import com.datashare.backend.entities.UserEntity;
import com.datashare.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

// Service = permet de dire à spring que cette classe est un service. 
// Transactional = tout ou rien. les opérations en base de donnée dans cette classe se font par le biais de transaction. Si problème en cours de route ça plante. 
// Slf4j = c'est lombok pour générer automatiquement un logger (log)

// Final = les dépendances sont obligatoires et ne changent jamais. 
// UserRepository = pour sauvegarder l'utilisateur en base
// PasswordEncoder = pour hasher le mot de passe avant de le sauvegarder 


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


// pour l'enregistrement d'un utilisateur. 
// Assert.notNull(user, "User must not be null") = vérifie que l'objet user ne soit pas null. 
// log info = comme un journal de bord, il retrace juste pour dire qu'il est en train d'enregister un nouvel utilisateur. 


    public void register(UserEntity user) {
        Assert.notNull(user, "User must not be null");
        log.info("Registering new user");


// Ici le but est de vérifier que l'email n'existe pas déja. 
// En prenant les information de user repository, prendre l'email, par l'utilisateur et son mail. 
// Si l'utilisateur existe déja et qu'il est présent, cela devient une erreur et renvoir un message "email existe déja".


    Optional<UserEntity> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }


// prend le mot de passe et l'encode pour le hasher
// ensuite sauvegarde l'utilisateur dans la base de donnée (modification en mémoire (RAM) = userRepository.save(user);
// setPassword est une méthode qui va pouvoir modifier la valeur du champ password. 
// user.getPassword = récupère le mot de passe en clair. 
// passwordEncoder.encode = hashe le mot de passe. 
// user.setPassword = remplace le mot de passe en clair par le hash

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }


// sauvegarder en base de données
        UserEntity savedUser = userRepository.save(user);


// Construire et retourner le DTO
        UserResponseDTO response = new UserResponseDTO();
        response.setId(savedUser.getId());
        response.setEmail(savedUser.getEmail());
        response.setCreatedAt(savedUser.getCreatedAt());
        return response;

}
