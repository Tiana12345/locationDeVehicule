package com.accenture.repository.entity;

import com.accenture.model.param.Permis;
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
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue(value = "MOTO")

public class Moto extends Vehicule {
    private Integer nombreCylindres;
    private Integer poids;
    private Integer puissanceEnkW;
    private Integer hauteurSelle;
    private String transmission;
    private List<Permis> listePermis;
}
