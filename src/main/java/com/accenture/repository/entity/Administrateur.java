package com.accenture.repository.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@DiscriminatorValue(value = "ADMIN")

public class Administrateur extends Utilisateur {


    private String fonction;

    public Administrateur(String mail, String password, String nom, String prenom) {
        super(mail, password, nom, prenom);
    }
}

