package com.accenture.repository.entity;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Est la classe utilisée pour créer les attributs nécéssaire aux administrateurs
 * Voir classe mère {@link Utilisateur}
 *
 * @author tatiana.m.tessier
 * @since 1.0
 */

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@DiscriminatorValue(value = "ROLE_ADMIN")

public class Administrateur extends Utilisateur {


    private String fonction;

    public Administrateur(String mail, String password, String nom, String prenom) {
        super(mail, password, nom, prenom);
    }
}

