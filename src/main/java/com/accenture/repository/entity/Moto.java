package com.accenture.repository.entity;

import com.accenture.model.param.Permis;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
    @Enumerated(EnumType.STRING)
    private Permis permis;
}
