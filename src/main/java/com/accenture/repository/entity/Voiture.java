package com.accenture.repository.entity;

import com.accenture.model.Carburant;
import com.accenture.model.Permis;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@DiscriminatorValue(value = "VOITURE")

public class Voiture extends Vehicule{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private int nombreDePlace;
    private int nombreDePorte;
    private String transmission;
    private Boolean clim;
    private int nombreDeBagage;
    private List<Permis> listePermis;
    private Carburant carburant;
    private long tarifJournalier;
    private long kilometrage;
    private Boolean actif;
    private Boolean retireDuParc;
}
