package com.accenture.service;

import com.accenture.repository.entity.Vehicule;

import java.util.List;

public interface VehiculeService {
    List<Vehicule> trouverToutVehicules();

    List<Vehicule> rechercher(Boolean actif, Boolean retireDuParc);
}
