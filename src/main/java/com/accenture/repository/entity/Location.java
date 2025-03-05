package com.accenture.repository.entity;

import com.accenture.model.param.Accessoires;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicule_id")
    private Vehicule vehiculeId;

    @Enumerated(EnumType.STRING)
    private Accessoires accessoires;

    private LocalDate dateDebut;
    LocalDate dateFin;
    private int kilometresParcourus;
    private int montantTotal;
    private LocalDate dateValidation;
    @Enumerated(EnumType.STRING)
    private Etat etat;

}
