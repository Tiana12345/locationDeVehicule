package com.accenture.repository;

import com.accenture.repository.entity.Vehicule;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@DiscriminatorValue(value = "CAMPING_CAR")


public class CampingCar extends Vehicule {
}
