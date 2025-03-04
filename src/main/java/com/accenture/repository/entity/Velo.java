package com.accenture.repository.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@DiscriminatorValue(value = "VELO ")

public class Velo extends Vehicule {
    private Integer tailleCadre;
    private Integer poids;
    private Boolean electrique;
    private Integer capaciteBatterie;
    private Integer autonomie;
    private Boolean freinsADisque;

}
