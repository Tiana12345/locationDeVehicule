package com.accenture.repository.entity;

/**
 * Est la classe utilisée pour créer les attributs nécéssaire aux administrateurs
 * classe fille {@link com.accenture.repository.entity.Client}
 * classe fille {@link com.accenture.repository.entity.Administrateur}
 * @author tatiana.m.tessier
 * @since 1.0
 */

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "Utilisateurs")
@DiscriminatorColumn(name = "DISCR")
@DiscriminatorValue(value = "USER")

public class Utilisateur {
    @Id
    private String mail;
    private String password;
    private String nom;
    private String prenom;
}
