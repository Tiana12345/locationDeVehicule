package com.accenture.repository.entity;

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
