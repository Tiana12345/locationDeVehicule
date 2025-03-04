package com.accenture.repository.entity;

import com.accenture.model.param.Etat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity

public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Client client;
    private Vehicule vehicule;
    private Accessoire accessoire;
    private LocalDate dateDebut;
    LocalDate dateFin;
    private int kilometresParcourus;
    private int montantTotal;
    private LocalDate dateValidation;
    @ElementCollection
    private Etat etat;

}
