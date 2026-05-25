package com.datashare.backend.service;

// AuthServiceTest teste la logique métier de la connexion
// Flux : LoginDTO → AuthService → UserRepository → PasswordEncoder → JwtService → token JWT

// Imports liés au projet
import com.datashare.backend.dto.LoginDTO;
import com.datashare.backend.entities.UserEntity;
import com.datashare.backend.repository.UserRepository;
import com.datashare.backend.security.JwtService;

// Imports liés aux tests
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

// @Mock = crée de faux objets qui simulent le comportement sans aller en BDD
// @InjectMocks = crée le vrai AuthService et injecte les faux objets

@ExtendWith(SpringExtension.class)
public class AuthServiceTest {

    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "password123";
    private static final String TOKEN = "fake.jwt.token";

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private AuthService authService;

    // Arrange = initialiser tous les entrants
    // Act = exécuter le système à tester
    // Assert = valider les sortants

    @Test
    public void test_login_user_not_found_throws_IllegalArgumentException() {
        // ARRANGE : l'utilisateur n'existe pas en base
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(EMAIL);
        loginDTO.setPassword(PASSWORD);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        // ACT + ASSERT : doit lancer une exception
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> authService.login(loginDTO));
    }

    @Test
    public void test_login_wrong_password_throws_IllegalArgumentException() {
        // ARRANGE : l'utilisateur existe mais le mot de passe est mauvais
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(EMAIL);
        loginDTO.setPassword(PASSWORD);

        UserEntity user = new UserEntity();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        // ACT + ASSERT : doit lancer une exception
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> authService.login(loginDTO));
    }

    @Test
    public void test_login_successful_returns_token() {
        // ARRANGE : l'utilisateur existe et le mot de passe est correct
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(EMAIL);
        loginDTO.setPassword(PASSWORD);

        UserEntity user = new UserEntity();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtService.generateToken(any())).thenReturn(TOKEN);

        // ACT : on tente la connexion
        String token = authService.login(loginDTO);

        // ASSERT : on vérifie que le token est retourné
        assertThat(token).isEqualTo(TOKEN);
    }
}