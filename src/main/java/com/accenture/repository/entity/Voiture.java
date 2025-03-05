package com.accenture.repository.entity;

import com.accenture.model.param.Carburant;
import com.accenture.model.param.Permis;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Voiture extends Vehicule {


    private Integer nombreDePlaces;
    private Integer nombreDePortes;
    private String transmission;
    private Boolean clim;
    private Integer nombreDeBagages;
    @Enumerated(EnumType.STRING)
    private Permis permis;
    @Enumerated(EnumType.STRING)
    private Carburant carburant;

}
