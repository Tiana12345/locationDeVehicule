package com.accenture.repository.entity;

import com.accenture.model.paramVehicule.Carburant;
import com.accenture.model.paramVehicule.Permis;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
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
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue(value = "UTILITAIRE")

public class Utilitaire extends Vehicule{
    private Integer nombreDePlace;
    private Carburant carburant;
    private String transmission;
    private Boolean clim;
    private Integer chargeMax;
    private Integer poidsPATC;
    private Integer capaciteM3;
    @ElementCollection
    private List<Permis> listePermis;

}
