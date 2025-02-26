package com.accenture.repository;

import com.accenture.model.paramVehicule.Carburant;
import com.accenture.model.paramVehicule.Permis;
import com.accenture.repository.entity.Voiture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoitureDao extends JpaRepository<Voiture, Long> {

}
