// Voir la fin pour la logique de flux avec le SecurityConfig

package com.datashare.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/register", "/api/login").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}


// Pour l'inscription : 

// SecurityConfig (vérifie que /api/register est publique → laisse passer)
//Front
//  → POST /api/register (email + password en JSON)
//    → UserController (@RequestBody convertit JSON en RegisterDTO)
//      → UserDTOMapper (convertit RegisterDTO en UserEntity)
//        → UserService
//          → UserRepository → BDD (vérifie si email existe)
//          → PasswordEncoder (hashe le mot de passe)
//          → UserRepository → BDD (sauvegarde l'utilisateur)
//        ← retourne UserResponseDTO (id + email + createdAt)
//      ← 201 Created



// Pour la connexion : 


//SecurityConfig (vérifie que /api/login est publique → laisse passer)
// Front
//  → POST /api/login (email + password en JSON)
//    → AuthController (@RequestBody convertit JSON en LoginDTO)
//      → AuthService
//        → UserRepository → BDD (cherche l'utilisateur par email)
//        ← retourne UserEntity
//        → PasswordEncoder (vérifie le mot de passe)
//        → JwtService (génère le token JWT)
//      ← retourne le token JWT
//    ← 200 OK + token JWT



// Requête protégée (après connexion) 


// SecurityConfig (vérifie que la route est protégée → passe par JwtFilter)
//Front (envoie token dans header Authorization: Bearer token)
//  → JwtFilter (intercepte la requête)
//    → JwtService (extrait et valide le token)
//      → UserRepository (vérifie que l'utilisateur existe)
//    → SecurityContext (authentifie l'utilisateur)
//  → Suite normale de la requête