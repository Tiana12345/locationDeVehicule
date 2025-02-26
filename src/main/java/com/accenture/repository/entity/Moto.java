package com.accenture.repository.entity;

import com.accenture.model.paramVehicule.Permis;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@DiscriminatorValue(value = "MOTO")

public class Moto extends Vehicule{
    private int nombreCylindres;
    private int poids;
    private int puissanceEnkW;
    private int hauteurSelle;
    private String transmission;
    private List<Permis> listePermis;
}
