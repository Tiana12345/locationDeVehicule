package com.accenture.repository.entity;

import com.accenture.model.paramVehicule.Carburant;
import com.accenture.model.paramVehicule.Permis;
import jakarta.persistence.*;
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
public class Voiture extends Vehicule{


    private Integer nombreDePlaces;
    private Integer nombreDePortes;
    private String transmission;
    private Boolean clim;
    private Integer nombreDeBagages;
    @ElementCollection
    private List<Permis> listePermis;
    private Carburant carburant;

}
