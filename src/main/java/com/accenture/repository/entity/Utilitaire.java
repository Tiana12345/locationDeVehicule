package com.accenture.repository.entity;

import com.accenture.model.Carburant;
import com.accenture.model.Permis;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@DiscriminatorValue(value = "UTILITAIRE")

public class Utilitaire extends Vehicule{
    private int nombreDePlace;
    private Carburant carburant;
    private String transmission;
    private Boolean clim;
    private int chargeMax;
    private int poidsPATC;
    private int capaciteM3;
    private List<Permis> listePermis;

}
