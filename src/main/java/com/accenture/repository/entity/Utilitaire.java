package com.accenture.repository.entity;

import com.accenture.model.param.Carburant;
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
@DiscriminatorValue(value = "UTILITAIRE")

public class Utilitaire extends Vehicule {
    private Integer nombreDePlace;
    @Enumerated(EnumType.STRING)
    private Carburant carburant;
    private String transmission;
    private Boolean clim;
    private Integer chargeMax;
    private Double poidsPATC;
    private Integer capaciteM3;
    @Enumerated(EnumType.STRING)
    private Permis permis;

}
