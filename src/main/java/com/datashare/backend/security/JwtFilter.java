package com.datashare.backend.security;

import com.datashare.backend.entities.UserEntity;
import com.datashare.backend.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

// JwtFilter intercepte chaque requête HTTP
// Il vérifie si le token JWT est présent et valide
// Si valide → accès autorisé
// Si invalide ou absent → accès refusé

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Récupère le header Authorization
        String authHeader = request.getHeader("Authorization");

        // 2. Vérifie que le header commence par "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extrait le token
        String token = authHeader.substring(7);

        // 4. Extrait l'email depuis le token
        String email = jwtService.extractEmail(token);

        // 5. Vérifie que l'utilisateur existe en base
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Optional<UserEntity> optionalUser = userRepository.findByEmail(email);

            if (optionalUser.isPresent()) {
                UserEntity user = optionalUser.get();

                // 6. Valide le token
                if (jwtService.isTokenValid(token, user)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}