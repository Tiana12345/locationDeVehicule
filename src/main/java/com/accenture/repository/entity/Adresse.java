package com.accenture.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe utilisée pour gérer l'adresse nécéssaire à l'inscription des {@link Client}
 *
 * @author tatiana.m.tessier
 * @since 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Adresse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "la rue ne doit pas être vide")
    private String rue;
    @NotBlank(message = "le code postal ne doit pas être vide")
    private String codePostal;
    @NotBlank(message = "la ville ne doit pas être vide")
    private String ville;

    public Adresse(String rue, String codePostal, String ville) {
    }
}
