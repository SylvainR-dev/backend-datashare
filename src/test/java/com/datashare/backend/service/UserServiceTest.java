package com.datashare.backend.service;

// Ici je teste le UserService, mais logiquement il dépend d'autres fichiers. 
// Raison pour laquelle il y a ces imports dessous. 

import com.datashare.backend.entities.UserEntity;
import com.datashare.backend.repository.UserRepository;
import com.datashare.backend.dto.UserResponseDTO;
import com.datashare.backend.service.UserService;

// Ensuite tous les imports liés au tests. 

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// @Mock = crée de faux objets (UserRepository, PasswordEncoder) qui simulent le 
// comportement de la base de données sans vraiment y aller. 

// @InjectMocks = crée le vrai UserService et lui injecte automatiquement 
// les faux objets créés par @Mock


// La stratégie de test pour la création d'un utilisateur. 

// 1) il faut savoir si l'utilisateur n'a pas de champ innaproprié. S'il n'est pas null. On passe null au service, il doit lancer une exception
// 2 )  S'il n'existe pas déja. En simulant un email déjà en base avec Mockito, il doit lancer une IllegalArgumentException
// 3) Puis le test de la création d'un utilisateur. En passant un utilisateur valide, il doit retourner un UserResponseDTO avec les bonnes données


@ExtendWith(SpringExtension.class)
public class UserServiceTest {
    private static final String EMAIL = "EMAIL";
    private static final String PASSWORD = "PASSWORD";
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

// AAA
// ARRANGE = initialiser tous les entrants
// ACT = exécuter le système à tester
// ASSERT = Valider les sortants


    @Test
    public void test_create_null_user_throws_IllegalArgumentException() {
        // ARRANGE = le but et qu'il doit lancer une exeption. Donc logiquement pas d'entrant ici. 

        // ACT = le but est de lancer un IllegalArgumentException
        Assertions.assertThrows(IllegalArgumentException.class,

                // ASSERT = doit renvoyer null
                () -> userService.register(null));
    }


    @Test
    public void test_create_already_exist_user_throws_IllegalArgumentException() {
        
        // ARRANGE = dans les entrants on va initialiser les éléments relatifs à la création de compte. 
        UserEntity user = new UserEntity();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(PASSWORD);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));

        // ACT = Ici il doit déclencher un IllegalArgumentException car l'utilisateur existe déja dans ce test
        Assertions.assertThrows(IllegalArgumentException.class,

                // ASSERT
                () -> userService.register(user));
    }

    @Test
    public void test_create_user() {
        // ARRANGE = on initialise les entrants pour la création d'un user. 
        UserEntity user = new UserEntity();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(PASSWORD);
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(user);

        // ACT = on fait l'action d'enregistrer l'utilisateur. 
        UserResponseDTO response = userService.register(user);

        // ASSERT = on vérifie que la réponse contient les bonnes données
        assertThat(response.getEmail()).isEqualTo(EMAIL);
    }
}
