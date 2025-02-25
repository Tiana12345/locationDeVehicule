package com.accenture.repository.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@DiscriminatorValue(value = "VELO ")

public class Velo extends Vehicule{
    private int tailleCadre;
    private int poids;
    private Boolean electrique;
    private int capaciteBatterie;
    private int autonomie;
    private Boolean freinsADisque;

    }
