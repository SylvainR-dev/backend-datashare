package com.datashare.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


// NoArgsConstructor = lorsqu'il y a une demande, on part de zero avec un constructeur vide, on va prendre l'information puis on la restitue.
// AllArgsConstructor = créé un objet déja rempli, utile pour les test notamment. 
// Entity = est là pour dire que cette classe est une table en base de données. Sinon il ignore tout. 
// Data est lombok = qui permet de générer les get, les set et autres.
// Table user = C'est le nom de la table dans PostgreSQL
// UserDetails = une interface de Spring Security. Oblige la classe à implémenter des méthodes de sécurité. 


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "users")
public class UserEntity implements UserDetails {


// ID est un élement spécial. Il faut que ce soit la clé primaire de la classe. Ce à quoi elle est rattachée. 
// Column(name = "id") = précise le nom de la colonne dans la base de donnée. 
// GeneratedValue = dit à PostgreSQL de générer automatiquement l'id à chaque nouvel enregistrement.
// Long (avec majuscule) = permet de dire que l'objet est créé mais pas encore sauvegardé en base, l'id est null


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


// NotBlank = vérifie que le champ n'est pas vide.
// Dans column il faut ajouter unique true car 2 utilisateurs ne peuvent pas avoir le même mail. 
// private = accessible uniquement depuis l'intérieur de la classe. 


@NotBlank
@Column(name = "email", unique = true, nullable = false)
private String email;

@NotBlank
@Column(name = "password", nullable = false)
private String password;


// @CreationTimestamp = remplit automatiquement la date à la création
// @UpdateTimestamp = remplit automatiquement la date à chaque modification

@CreationTimestamp
@Column(name = "created_at")
private LocalDateTime createdAt;

@UpdateTimestamp
@Column(name = "updated_at")
private LocalDateTime updatedAt;




@Override
public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
}


// on utilise l'email comme identifiant

@Override
public String getUsername() {
    return email; 
}

@Override
public String getPassword() {
    return password;
}


@Override
public boolean isAccountNonExpired() {
    return true;
}

@Override
public boolean isAccountNonLocked() {
    return true;
}

@Override
public boolean isCredentialsNonExpired() {
    return true;
}

@Override
public boolean isEnabled() {
    return true;
}

}